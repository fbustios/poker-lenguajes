package network.io;

import network.ServerEvent;
import network.ServerMessage;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public final class PlayerConnection implements Runnable{
    private final Socket socket;
    public final int CONST = 1;
    private final BlockingQueue<ClientMessage> outputQueue;
    private final BlockingQueue<String> inputQueue;
    private final InputStream inputStream;
    private final RequestParser requestParser;
    private boolean alive;

    public PlayerConnection(Socket socket, BlockingQueue<ClientMessage> outputQueue, RequestParser requestParser) throws IOException {
        this.socket = socket;
        this.inputQueue = new LinkedBlockingQueue<>();
        this.inputStream = socket.getInputStream();
        this.outputQueue = outputQueue;
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
                    System.out.println(message.get().event());
                    System.out.println(outputQueue.size());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        //System.out.println(message.get().event().getAction());

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

    }

    public boolean isAlive() {
        return this.alive;
    }

    public void addToEventQueue(String message) {
        inputQueue.add(message);
    }


}
