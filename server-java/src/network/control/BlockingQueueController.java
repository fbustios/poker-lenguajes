package network.control;

import network.ClientEvent;
import network.io.Connection;
import network.io.EventEmitter;
import network.ServerEvent;
import network.ServerMessage;
import network.io.ClientMessage;
import poker.GameState;
import poker.PokerGame;
import poker.gamemodes.PlayerAction;
import poker.gamemodes.PokerAction;
import poker.items.Player;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;


public final class BlockingQueueController implements Controller{
    private final EventEmitter pokerEventEmitter;
    private final BlockingQueue<ClientMessage> eventQueue;
    private final PokerGame game;
    private final ConnectionPlayerMapping connectionMap;
    public BlockingQueueController(final EventEmitter pokerEventEmitter,
                                   final BlockingQueue<ClientMessage> eventQueue,
                                   final PokerGame game,
                                   final ConnectionPlayerMapping connectionMap) {
        this.pokerEventEmitter = pokerEventEmitter;
        this.eventQueue = eventQueue;
        this.game = game;
        this.connectionMap = connectionMap;
    }


    public void start() {
        ServerEvent startEvent = ServerEvent.GAME_STARTED;
        //falta clase que construya los mensajes del server;
        ServerMessage startMessage = new ServerMessage();
        // game.startGame();
        sendMessage(ServerEvent.GAME_STARTED);
        /*
        while(true) {
            try {
                Optional<ClientMessage> event = Optional.ofNullable(eventQueue.poll(100, TimeUnit.MILLISECONDS));
                //if (game.isNotStarted) serverEvent update y mando con el jugador que tiene que jugar
                if (game.isGamemodeOver()) {
                    sendMessage(ServerEvent.MODE_CHANGED);
                }
                if (event.isPresent()) {
                    processEvent(event.get());
                }
                if (game.isGameFinished()) {
                    sendMessage(ServerEvent.GAME_ENDED);
                    return;
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }

        }
         */
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
            System.out.println("Player is here, not skipped");
        }
        sendMessage(ServerEvent.ROUND_UPDATE);
    }
    //same aqui
    private void sendMessage(ServerEvent event) {
        switch (event) {
            case GAME_STARTED -> buidGameStartedMessage();
            case ROUND_UPDATE -> buildRoundUpdateMessage();
            case GAME_ENDED -> buildGameEndedMessage();
        }
    }

    private void buildGameEndedMessage() {
        Player winner = game.getWinner();
        List<Connection> connections = connectionMap.getConnections();
        String playerName = " ";
        int money = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("event: game_ended\n");
        sb.append("winner: " + playerName + "\n");
        sb.append("money_won: " + 0 + "\n");
        pokerEventEmitter.emit(connections, sb.toString());
    }

    private void buildRoundUpdateMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("event: game_ended\n");
        GameState pokerGameState = game.getGameState();
        List<Player> activePlayers = pokerGameState.getPlayers();
        //length(mensaje)
        //event: update_round
        //gamemode: mode
        //gamemode_round: round
        //pot: p
        //next_player: name
        //dealer: name
        //players_left: n
        //n1: C5, CS, ?, ?, money
        //n2: DK, S4, ?, ?, money
    }

    private void buidGameStartedMessage() {
        List<Connection> connections = connectionMap.getConnections();
        //Optional<Player> playerOptional = game.nextTurn();
        String playerName = " ";
        StringBuilder sb = new StringBuilder();
        sb.append("event: game_started\n");
        sb.append("next-player: " + playerName + "\n");
        String playerList = ",,,";
        sb.append("players: " + playerList + "\n");
        pokerEventEmitter.emit(connections, sb.toString());
    }

}
