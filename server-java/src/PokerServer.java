import network.ClientEvent;
import network.control.BlockingQueueController;
import network.control.Controller;
import network.control.SequenceConnectionPlayerMapping;
import network.io.*;
import poker.HorsePokerGame;
import poker.PokerGame;
import poker.gamemodes.HoldemPokerGamemode;
import poker.items.Player;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

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
            Map<Connection, Player> playerConnections = gatherConnections(serverSocket,queue);
            System.out.println("Lobby built");
            PokerGame game = new HorsePokerGame(List.of(new HoldemPokerGamemode(null, null, null, null)), null);
            Controller gameController = new BlockingQueueController(new PokerEventEmitter(), queue,game, SequenceConnectionPlayerMapping.build(playerConnections));
            gameController.start();
        } catch (Exception e) {
            System.err.println("Could not listen on port " + this.port);
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }

    Map<Connection, Player> gatherConnections (ServerSocket socket, BlockingQueue<ClientMessage> queue) {
        try {
            RequestParser rq = new LineScannerRequestParser();
            int currentPlayers = 0;
            Map<Connection, Player> playerConnections = new HashMap<>();
            while(currentPlayers < this.maxPlayers) {
                Socket playerSocket = socket.accept();
                currentPlayers++;
                Connection playerConnection = new PlayerConnection(playerSocket, queue,rq);
                new Thread(playerConnection).start();
                boolean sent = false;
                while (!sent) {  //deberia ser toda una responsabilidad aparte, crear lobbys, pero bueno
                    Optional<ClientMessage> event = Optional.ofNullable(queue.poll(100, TimeUnit.MILLISECONDS));
                    if(event.isPresent()) {
                        ClientMessage message = event.get();
                        if (message.event().equals(ClientEvent.JOIN_GAME)) {
                            sent = true;
                            Player p = new Player(message.author(),List.of(),Integer.parseInt(message.details().get("bet")));
                            playerConnections.put(playerConnection,p);
                        }
                    }
                }
            }
            return playerConnections;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
