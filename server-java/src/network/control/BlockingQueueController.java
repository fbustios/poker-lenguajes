package network.control;

import network.ClientEvent;
import network.io.Connection;
import network.io.EventEmitter;
import network.io.PlayerConnection;
import network.ServerEvent;
import network.ServerMessage;
import network.io.ClientMessage;
import poker.PokerGame;
import poker.gamemodes.PlayerAction;
import poker.gamemodes.PokerAction;
import poker.items.Player;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public final class BlockingQueueController implements Controller{
    private final EventEmitter pokerEventEmitter;
    private final List<PlayerConnection> connections;
    private final BlockingQueue<ClientMessage> eventQueue;
    private final PokerGame game;
    private final ConnectionPlayerMapping connectionMap;
    public BlockingQueueController(final EventEmitter pokerEventEmitter,
                                   final List<PlayerConnection> connections,
                                   final BlockingQueue<ClientMessage> eventQueue,
                                   final PokerGame game,
                                   final ConnectionPlayerMapping connectionMap) {
        this.pokerEventEmitter = pokerEventEmitter;
        this.connections = connections;
        this.eventQueue = eventQueue;
        this.game = game;
        this.connectionMap = connectionMap;
    }


    public void start() {
        ServerEvent startEvent = ServerEvent.GAME_STARTED;
        //falta clase que construya los mensajes del server;
        ServerMessage startMessage = new ServerMessage();
        sendEvent(startMessage);
        game.startGame();
        while(true) {
            try {
                Optional<ClientMessage> event = Optional.ofNullable(eventQueue.poll(100, TimeUnit.MILLISECONDS));
                //if (game.isNotStarted) serverEvent update y mando con el jugador que tiene que jugar
                if (game.isGamemodeOver()) {
                    ServerEvent serverEvent = ServerEvent.MODE_CHANGED; //sendEvent();
                }
                if (event.isPresent()) {
                    processEvent(event.get());
                }
                if (game.isGameFinished()) {
                    Player winner = game.getWinner();
                    ServerEvent serverEvent = ServerEvent.GAME_ENDED;
                    //llama a sendEvent
                    return;
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }

        }
    }



    private void processEvent(final ClientMessage message) {
        ClientEvent event = message.event();
        //aqui deberia ser un map con las clases que se encrgan de procesar cada accion, todas con una interfaz comun
        //pero no hay tiempo entonces le voy a meter un switch
        switch (event) {
            case ACTION -> handleAction(message);
            case LEAVE_GAME -> handleLeaveGame(message);
            case JOIN_GAME -> handleJoinGame(message);
        }
    }

    private void handleJoinGame(ClientMessage message) {
        System.out.println("Not valid"); //de momento
    }

    private void handleLeaveGame(ClientMessage message) {
        String author = message.author();
        Optional<Player> player = connectionMap.getPlayerFromName(author);
        Optional<Connection> connection = connectionMap.getConnectionFromName(author);
        if (player.isPresent() && connection.isPresent()) {
            player.get().setActive(false);
            connection.get().setAlive(false);
        }
    }

    private void handleAction(ClientMessage message) {
        String author = message.author();
        Optional<Player> playerModel = connectionMap.getPlayerFromName(message.author());
        if (playerModel.isPresent()) {
            PokerAction action = new PokerAction(playerModel.get(), PlayerAction.CALL, 2);
            game.play(action);
            Optional<Player> nextPlayer = game.nextTurn();
            String parsedAction = "x";
            pokerEventEmitter.emit(connections, parsedAction);
        }
    }

    private void sendEvent(final ServerMessage message) {
        //primero parsear mensaje;
        String mes = "ms";
        pokerEventEmitter.emit(connections,mes);
    }
}
