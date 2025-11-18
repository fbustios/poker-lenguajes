import network.control.BlockingQueueController;
import network.control.Controller;
import network.PlayerConnection;
import network.io.LineScannerRequestParser;
import network.io.RequestParser;
import poker.items.PlayerModel;
import poker.table.CircularLinkedListPokerTable;
import poker.table.PokerTable;
import poker.table.Seat;

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
            System.out.println("Servidor escuchando en el puerto " + port + "...");
            BlockingQueue<String> queue = new LinkedBlockingQueue<>();
            RequestParser rq = new LineScannerRequestParser();
            int currentPlayers = 0;
            List<PlayerConnection> playerConnections = new ArrayList<>();
            while(currentPlayers < this.maxPlayers) {
                Socket playerSocket = serverSocket.accept();
                currentPlayers++;
                PlayerConnection playerConnection = new PlayerConnection(playerSocket, queue, rq);
                playerConnections.add(playerConnection);
                new Thread(playerConnection).start();
            }
            PokerTable table = new CircularLinkedListPokerTable(new Seat(2, new PlayerModel(1, List.of())));
            Controller gameController = new BlockingQueueController(queue, table);
            //gameController.start();
            for(int i = 0; i < currentPlayers; i++ ) {
                System.out.println(playerConnections.get(i).CONST);
            }

        } catch (IOException e) {
            System.err.println("Could not listen on port " + this.port);
            System.exit(-1);
        }
    }


}
