import java.io.IOException;
import java.net.ServerSocket;

public final class PokerServer {
    private static final int ARGS_COUNT = 2;
    private final int port;
    private final int maxPlayers;

    public PokerServer(final int port, final int players) {
        this.port = port;
        this.maxPlayers = players;
    }

    static void main(String[] args) {

    }

    void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            int currentPlayers = 0;
            while(currentPlayers < this.maxPlayers) {
                new Thread();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + this.port);
            System.exit(-1);
        }
    }


}
