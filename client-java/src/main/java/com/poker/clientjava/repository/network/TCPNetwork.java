package com.poker.clientjava.repository.network;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public final class TCPNetwork implements NetworkClient {
    private final EventDispatcher dispatcher = new EventDispatcher();
    private final BlockingQueue<byte[]> sendQueue = new LinkedBlockingQueue<>();
    private final AtomicBoolean running = new AtomicBoolean(false);

    private Socket socket;
    private Thread readerThread;
    private Thread writerThread;

    private final int readTimeoutMs = 0;

    @Override
    public void connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            socket.setTcpNoDelay(true);
            socket.setSoTimeout(readTimeoutMs);

            writerThread = new Thread(this::writerLoop, "net-writer");
            writerThread.setDaemon(true);
            writerThread.start();

            readerThread = new Thread(this::readerLoop, "net-reader");
            readerThread.setDaemon(true);
            readerThread.start();

            dispatcher.fireConnected();
        } catch (Exception e) {
            throw new RuntimeException("Connecting socket and threads" + e);
        }
    }

    @Override
    public void disconnect() {
        running.set(false);
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Socket closing error" + e);
        }
    }

    @Override
    public boolean isConnected() {
        return socket != null &&
                socket.isConnected() &&
                !socket.isClosed();
    }

    @Override
    public void sendText(String text) {
        sendBytes(MessageCodec.textToBytes(text));
    }

    @Override
    public void sendBytes(byte[] bytes) {
        try {
            byte[] framed = MessageCodec.encode(bytes);
            sendQueue.put(framed);
        } catch (InterruptedException e) {
            throw new RuntimeException("message encoding error " + e);
        }
    }

    private void writerLoop() {
        try (OutputStream out = socket.getOutputStream()) {
            while (running.get() && !Thread.currentThread().isInterrupted()) {
                final byte[] messageToSend = sendQueue.take();
                out.write(messageToSend);
                out.flush();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            handleError(e);
        }
    }

    private void readerLoop() {
        try (InputStream in = socket.getInputStream()) {
            DataInputStream dis = new DataInputStream(in);

            while (running.get() && !Thread.currentThread().isInterrupted()) {
                final int length = extractLength(dis);

                if (length < 0 || length > 10_000_000) {
                    throw new IOException("Invalid message length: " + length);
                }

                byte[] payload = new byte[length];
                dis.readFully(payload);
                dispatcher.fireRaw(payload);

                final String text = MessageCodec.bytesToText(payload);
                dispatcher.fireText(text);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while reading socket and threads" + e);
        }
    }

    private void handleError(final Exception e) {
        running.set(false);
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ioe) {
            throw new RuntimeException("Socket closing error" + e);
        }

        dispatcher.fireDisconnected(e);
    }

    private int extractLength(final DataInputStream dis) {
        try {
            return dis.readInt();
        } catch (IOException e) {
            throw new RuntimeException("Error while reading length" + e);
        }
    }
}
