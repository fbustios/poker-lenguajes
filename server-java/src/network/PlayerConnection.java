package network;

import network.io.ClientMessage;
import network.io.RequestParser;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;

public final class PlayerConnection implements Runnable{
    private final Socket socket;
    public final int CONST = 1;
    private final BlockingQueue<ClientMessage> queue;
    private final InputStream inputStream;
    private final RequestParser requestParser;

    public PlayerConnection(Socket socket, BlockingQueue<ClientMessage> queue, RequestParser requestParser) throws IOException {
        this.socket = socket;
        this.queue = queue;
        this.inputStream = socket.getInputStream();
        this.requestParser = requestParser;
    }

    @Override
    public void run() {
        System.out.println("Connection running");
        try {
            while (true) {
                Optional<ClientMessage> message = read();
                if (message.isPresent()) {
                    queue.add(message.get());
                    System.out.println("added to the event queue");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }


        //System.out.println(message.get().event().getAction());

    }

    private Optional<ClientMessage> read() {
        try {
            DataInputStream dis = new DataInputStream(inputStream);
            int len = dis.readInt();
            byte[] data = new byte[len];
            dis.readFully(data);
            String event = new String(data);
            return requestParser.parse(event);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void write() {}
}
