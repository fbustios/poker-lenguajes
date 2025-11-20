package network.io;

import network.ServerMessage;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;

public final class PlayerConnection implements Runnable{
    private final Socket socket;
    public final int CONST = 1;
    private final BlockingQueue<ClientMessage> outputQueue;
    private final BlockingQueue<String> inputQueue;
    private final InputStream inputStream;
    private final RequestParser requestParser;
    private boolean alive;

    public PlayerConnection(Socket socket, BlockingQueue<ClientMessage> outputQueue, BlockingQueue<String> inputQueue, RequestParser requestParser) throws IOException {
        this.socket = socket;
        this.outputQueue = outputQueue;
        this.inputStream = socket.getInputStream();
        this.inputQueue = inputQueue;
        this.requestParser = requestParser;
        this.alive = true;
    }

    @Override
    public void run() {
        System.out.println("Connection running");
        try {
            while (true) {
                Optional<ClientMessage> message = read();
                if (message.isPresent()) {
                    outputQueue.add(message.get());
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

    public boolean isAlive() {
        return this.alive;
    }

    public void addToEventQueue(String message) {
        inputQueue.add(message);
    }


}
