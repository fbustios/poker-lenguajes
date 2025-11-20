import network.io.PlayerConnection;
import network.io.ClientMessage;
import network.io.LineScannerRequestParser;
import network.io.RequestParser;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public final class PokerServer {
    private static final int ARGS_COUNT = 2;
    private final int port;
    private final int maxPlayers;

    public PokerServer(final int port, final int players) {
        this.port = port;
        this.maxPlayers = players;
    }

    public static void main(String[] args) {
        final PokerServer app = new PokerServer(8080,1);
        app.run();
    }

    void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Listening on " + port + "...");
            BlockingQueue<ClientMessage> queue = new LinkedBlockingQueue<>();
            List<PlayerConnection> playerConnections = gatherConnections(serverSocket,queue);
            //PokerGame game = new HorsePokerGame();
            //Controller gameController = new BlockingQueueController(queue, game);
            //gameController.start();
            for(int i = 0; i < playerConnections.size(); i++ ) {
                System.out.println(playerConnections.get(i).CONST);
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + this.port);
            System.exit(-1);
        }
    }

    List<PlayerConnection> gatherConnections (ServerSocket socket, BlockingQueue<ClientMessage> queue) throws IOException {
        RequestParser rq = new LineScannerRequestParser();
        int currentPlayers = 0;
        List<PlayerConnection> playerConnections = new ArrayList<>();
        while(currentPlayers < this.maxPlayers) {
            Socket playerSocket = socket.accept();
            currentPlayers++;
            PlayerConnection playerConnection = new PlayerConnection(playerSocket, queue, rq);
            playerConnections.add(playerConnection);
            new Thread(playerConnection).start();
        }
        return playerConnections;
    }


}
