package network;

import network.io.RequestParser;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public final class PlayerConnection implements Runnable{
    private final Socket socket;
    public final int CONST = 1;
    private final BlockingQueue<String> queue;
    private final InputStream inputStream;
    private final RequestParser requestParser;

    public PlayerConnection(Socket socket, BlockingQueue<String> queue, RequestParser requestParser) throws IOException {
        this.socket = socket;
        this.queue = queue;
        this.inputStream = socket.getInputStream();
        this.requestParser = requestParser;
    }

    @Override
    public void run() {
        read();
        write();

    }

    private void read() {
        try {
            DataInputStream dis = new DataInputStream(inputStream);
            int len = dis.readInt();
            byte[] data = new byte[len];
            dis.readFully(data);
            String event = new String(data);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void write() {}
}
