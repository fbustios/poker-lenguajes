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
import java.util.Map;
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
                    System.out.println("cambie de modo bien");
                    game.setNextMode();
                    System.out.println("sali");
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
                System.out.println("aqui fue el error");
                System.out.println(e.getMessage());
                System.out.println(e.getStackTrace());
                System.out.println(e.getCause());
                System.out.println(e.fillInStackTrace());
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
        Map<String, String> details = message.details();
        Optional<Player> playerModel = connectionMap.getPlayerFromName(author);
        int bet = details.get("bet") == null ? 100 : Integer.parseInt(details.get("bet"));
        if (playerModel.isPresent()) {
            PokerAction action;
            switch (message.details().get("player_action")) {
                case "call" -> action = new PokerAction(playerModel.get(), PlayerAction.CALL, bet);
                case "check" -> action = new PokerAction(playerModel.get(), PlayerAction.CHECK, bet);
                case "all_in" -> action = new PokerAction(playerModel.get(), PlayerAction.ALL_IN, bet);
                case "fold" -> action = new PokerAction(playerModel.get(), PlayerAction.FOLD, bet);
                case "raise" -> action = new PokerAction(playerModel.get(), PlayerAction.RAISE, bet);
                default -> throw new IllegalStateException("no existe tal accion");
            }
            game.play(action);
            System.out.println("Player is here, not skipped");
        }
        System.out.println("antes del switch");
        sendMessage(ServerEvent.ROUND_UPDATE);
    }
    //same aqui
    private void sendMessage(ServerEvent event) {
        System.out.println("estoy en el switch");
        System.out.println(event.toString());
        switch (event) {
            case GAME_STARTED -> buidGameStartedMessage();
            case ROUND_UPDATE -> buildRoundUpdateMessage();
            case GAME_ENDED -> buildGameEndedMessage();
            case MODE_CHANGED -> buildModeChangedMessage();
        }
    }

    private void buildModeChangedMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("event: mode_changed\n");
        sb.append("game_mode: " + game.getGameState().getCurrentGamemode() + "\n");
        GameState pokerGameState = game.getGameState();
        List<Player> activePlayers = pokerGameState.getPlayers();
        sb.append("players_left: " + activePlayers.size() + "\n");
        for(int i = 0; i < activePlayers.size(); i++) {
            Player currentPlayer = activePlayers.get(i);
            StringBuilder hand = new StringBuilder();
            for(int j = 0; j < currentPlayer.getCards().size(); j++) {
                hand.append(currentPlayer.getCards().get(j).toString() + ",");
            }
            hand.append(currentPlayer.getMoney());
            hand.append("\n");
            sb.append(currentPlayer.getName() + ": " + hand);
        }

        pokerEventEmitter.emit(connectionMap.getConnections(),sb.length() + "\n" + sb);
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
        System.out.println("round update");
        Optional<Player> playerOptional = game.nextTurn();

        if (playerOptional.isEmpty()) return;
        String playerName = playerOptional.get().getName();
        //length(mensaje)
        sb.append("game_mode: " + pokerGameState.getCurrentGamemode() + "\n");
        sb.append("pot:" + pokerGameState.getPot() + "\n");
        String details = pokerGameState.getDetails();
        sb.append(details);
        sb.append("next_player: " + playerName + "\n");
        sb.append("players_left: " + activePlayers.size() + "\n");
        for(int i = 0; i < activePlayers.size(); i++) {
            Player currentPlayer = activePlayers.get(i);
            StringBuilder hand = new StringBuilder();
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
