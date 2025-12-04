package network.control;

import network.ClientEvent;
import network.io.Connection;
import network.io.EventEmitter;
import network.ServerEvent;
import network.io.ClientMessage;
import poker.GameState;
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
        //falta clase que construya los mensajes del server;
        game.startGame();
        sendMessage(ServerEvent.GAME_STARTED);

        while(true) {
            try {
                Optional<ClientMessage> event = Optional.ofNullable(eventQueue.poll(100, TimeUnit.MILLISECONDS));
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
        Optional<Player> playerModel = connectionMap.getPlayerFromName(author);
        if (playerModel.isPresent()) {
            PokerAction action;
            switch (message.details().get("player_action")) {
                case "call" -> action = new PokerAction(playerModel.get(), PlayerAction.CALL, 0);
                case "check" -> action = new PokerAction(playerModel.get(), PlayerAction.CHECK, 0);
                case "all_in" -> action = new PokerAction(playerModel.get(), PlayerAction.ALL_IN, 0);
                case "fold" -> action = new PokerAction(playerModel.get(), PlayerAction.FOLD, 0);
                default -> throw new IllegalStateException("no existe tal accion");
            }
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
            case MODE_CHANGED -> buildModeChangedMessage();
        }
    }

    private void buildModeChangedMessage() {

    }

    private void buildGameEndedMessage() {
        Player winner = game.getWinner();
        List<Connection> connections = connectionMap.getConnections();
        StringBuilder sb = new StringBuilder();
        sb.append("event: game_ended\n");
        sb.append("winner: " + winner.getName() + "\n");
        sb.append("money_won: " + 0 + "\n");
        pokerEventEmitter.emit(connections, sb.toString());
    }

    private void buildRoundUpdateMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("event: update_round\n");
        GameState pokerGameState = game.getGameState();
        List<Player> activePlayers = pokerGameState.getPlayers();
        Optional<Player> playerOptional = game.nextTurn();
        if (playerOptional.isEmpty()) throw new IllegalStateException();
        String playerName = playerOptional.get().getName();
        //length(mensaje)
        sb.append("gamemode: " + pokerGameState.getCurrentGamemode() + "\n");
        sb.append("gamemode_round: " + "\n");
        sb.append("pot:" + pokerGameState.getPot());
        sb.append("last_raise: " + "\n");
        sb.append("next_player: ");
        List<Connection> connections = connectionMap.getConnections();
        List<Player> players = connectionMap.getPlayers();
        String details = pokerGameState.getDetails();
        sb.append(details);
        sb.append("players_left: " + activePlayers.size() + "\n");
        sb.append("next-player: " + playerName + "\n");
        sb.append("players_left: " + players.size() + "\n");
        for(int i = 0; i < connections.size(); i++) {
            Player currentPlayer = players.get(i);
            StringBuilder hand = new StringBuilder();
            System.out.println("tamano: " + currentPlayer.getCards().size());
            for(int j = 0; j < currentPlayer.getCards().size(); j++) {
                    hand.append(currentPlayer.getCards().get(j).toString() + ",");
            }
            hand.append(currentPlayer.getMoney());
            hand.append("\n");
            sb.append(currentPlayer.getName() + ": " + hand);
        }

        pokerEventEmitter.emit(connectionMap.getConnections(),sb.length() + "\n" + sb);

    }

    private void buidGameStartedMessage() {
        List<Connection> connections = connectionMap.getConnections();
        List<Player> players = connectionMap.getPlayers();
        Optional<Player> playerOptional = game.nextTurn();
        if (playerOptional.isEmpty()) {
            throw new IllegalStateException("Game could not start: nextTurn() returned empty. Check TurnManager state.");
        }
        String playerName = playerOptional.get().getName();
        StringBuilder sb = new StringBuilder();
        sb.append("event: game_started\n");
        sb.append("game_mode: " + game.getGameState().getCurrentGamemode() + "\n");
        sb.append(game.getGameState().getDetails());
        sb.append("pot: " + game.getGameState().getPot() + "\n");
        sb.append("next_player: " + playerName + "\n");
        sb.append("players_left: " + players.size() + "\n");
        for(int i = 0; i < connections.size(); i++) {
            Player currentPlayer = players.get(i);
            StringBuilder hand = new StringBuilder();
            for(int j = 0; j < currentPlayer.getCards().size(); j++) {
                    hand.append(currentPlayer.getCards().get(j).toString() + ",");
            }
            hand.append(currentPlayer.getMoney());
            hand.append("\n");
            sb.append(currentPlayer.getName() + ": " + hand);
        }

        pokerEventEmitter.emit(connections, sb.length() + "\n" + sb.toString());
    }

}
