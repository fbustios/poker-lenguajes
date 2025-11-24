package network.io;

import java.io.*;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public final class PlayerConnection implements Connection{
    private final Socket socket;
    private final BlockingQueue<String> outputQueue;
    private final BlockingQueue<ClientMessage> clientEventQueue;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final RequestParser requestParser;
    private boolean alive;

    public PlayerConnection(final Socket socket,
                            final BlockingQueue<ClientMessage> clientEventQueue,
                            final RequestParser requestParser) throws IOException {
        this.socket = socket;
        this.clientEventQueue = clientEventQueue;
        this.inputStream = socket.getInputStream();
        this.outputQueue = new LinkedBlockingQueue<>();
        this.requestParser = requestParser;
        this.outputStream = socket.getOutputStream();
        this.alive = true;
    }

    @Override
    public void run() {
        System.out.println("Connection running");
        try {
            while (this.alive) {
                Optional<ClientMessage> message = read();
                if (message.isPresent()) {
                    clientEventQueue.add(message.get());
                    System.out.println(message.get().event());
                    System.out.println(outputQueue.size());
                }
                if (!outputQueue.isEmpty()) {
                    String event = outputQueue.poll();
                    write(event);
                }
            }
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public boolean isAlive() {
        return this.alive;
    }

    @Override
    public void setAlive(boolean val) {
        this.alive = val;
    }
    @Override
    public void addToOutputQueue(String message) {
        outputQueue.add(message);
    }

    private Optional<ClientMessage> read() {
        try {
            if (inputStream.available() != 0) {
                DataInputStream dis = new DataInputStream(inputStream);
                int len = dis.readInt();
                byte[] data = new byte[len];
                dis.readFully(data);
                String event = new String(data);
                return requestParser.parse(event);
            }
            return Optional.empty();

        } catch (Exception e) {
            System.out.println("error aca");
            throw new RuntimeException(e.getMessage());
        }

    }

    private void write(String event) {
        try {
            DataOutputStream dos = new DataOutputStream(outputStream);
            dos.writeInt(event.length());
            dos.writeBytes(event);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
