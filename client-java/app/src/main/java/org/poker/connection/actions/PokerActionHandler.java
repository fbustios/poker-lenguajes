package org.poker.connection.actions;

import org.poker.connection.messages.MessageParser;
import org.poker.connection.messages.MessageListener;
import org.poker.model.GameState;
import org.poker.model.PlayerModel;

import java.util.Map;
import java.util.Optional;

import static java.lang.Integer.parseInt;

public final class PokerActionHandler {
    private static final String EVENT = "event";
    private static final String GAME_MODE = "game_mode";
    private static final String NEXT_PLAYER = "next_player";
    private static final String DEALER = "dealer";
    private static final String SMALL_BLIND = "small_blind";
    private static final String BIG_BLIND = "big_blind";
    private static final String POT = "pot";
    private static final String PLAYER_PREFIX = "player_";
    private static final String WINNER_PREFIX = "winner_";
    private static final String PRIZE_PREFIX = "prize_";
    private static final String PLAYERS_LEFT = "players_left";
    private static final String GAME_MODE_ROUND = "game_mode_round";
    private static final String NEXT_ROUND = "next_round";

    private PokerActionHandler() {

    }

    public static void handleServerMessage(String message, GameState gameState, MessageListener messageListener) {
        final Map<String, String> data = MessageParser.parse(message);
        final Optional<String> event = Optional.ofNullable(data.get(EVENT));

        if (event.isEmpty()) {
            System.err.println("Mensaje sin evento");
            return;
        }
        switch (event.get()) {
            case "game_started":
                handleGameStarted(data, gameState);
                break;
            case "player_action":
                handlePlayerAction(data, gameState);
                break;
            case "round_over":
                handleRoundOver(data, gameState);
                break;
            case "game_ended":
                handleGameEnded(data);
                break;
            case "update_round":
                handleUpdateRound(data, gameState);
                break;
            default:
                System.out.println("Evento desconocido: " + event);
        }
        if (messageListener != null) {
            messageListener.onMessageReceived(event.get(), data);
        }
    }
    private static void handleGameStarted(Map<String, String> data, GameState gameState) {
        gameState.setGameMode(data.get(GAME_MODE));
        gameState.setNextPlayer(data.get(NEXT_PLAYER));
        gameState.setPlayers_left(Integer.parseInt(data.get(PLAYERS_LEFT)));
        gameState.setPot(Integer.parseInt(data.get(POT)));
        gameState.setDealer(data.get(DEALER));
        gameState.setSmallBlind(data.get(SMALL_BLIND));
        gameState.setBigBlind(data.get(BIG_BLIND));

        gameState.playersClear();
        int playerCount = 0;
        while (data.containsKey(PLAYER_PREFIX + playerCount)) {
            final PlayerModel pm = new PlayerModel();
            pm.setName(data.get(PLAYER_PREFIX + playerCount));
            pm.setIndex(playerCount);
            gameState.playersAdd(pm);
            playerCount++;
        }

        System.out.println("\n=== JUEGO INICIADO ===");
        System.out.println("Modo: " + gameState.getGameMode());
        System.out.println("Dealer: " + gameState.getDealer());
        System.out.println("Small Blind: " + gameState.getSmallBlind());
        System.out.println("Big Blind: " + gameState.getBigBlind());
        System.out.println("Jugadores: " + gameState.getPlayers().size());
        System.out.println("Siguiente: " + gameState.getNextPlayer());
    }

    private static void handlePlayerAction(Map<String, String> data, GameState gameState) {
        final String player = data.get("player");
        final String nextPlayer = data.get(NEXT_PLAYER);
        final String action = data.get("action");
        final String dealer = data.get(DEALER);
        final int pot = getInt(data, POT, gameState.getPot());

        gameState.setNextPlayer(nextPlayer);
        gameState.setDealer(dealer);
        gameState.setPot(pot);

        System.out.println("\n=== ACCIÓN DE JUGADOR ===");
        System.out.println("Jugador: " + player + " -> " + action);
        System.out.println("Pot: " + pot);
        System.out.println("Siguiente: " + nextPlayer);
    }

    private static void handleRoundOver(Map<String, String> data, GameState gameState) {
        final String gameMode = data.get(GAME_MODE);
        final String nextRound = data.get(NEXT_ROUND);
        final int pot = getInt(data, POT, gameState.getPot());

        gameState.setPot(pot);

        System.out.println("\n=== RONDA TERMINADA ===");
        System.out.println("Modo: " + gameMode);
        System.out.println("Siguiente ronda: " + nextRound);
        System.out.println("Pot: " + pot);
    }

    private static void handleGameEnded(Map<String, String> data) {
        final String gameMode = data.get(GAME_MODE);

        System.out.println("\n=== JUEGO TERMINADO ===");
        System.out.println("Modo: " + gameMode);
        System.out.println("Ganadores:");

        int winnerCount = 0;
        while (data.containsKey(WINNER_PREFIX + winnerCount)) {
            final String winner = data.get(WINNER_PREFIX + winnerCount);
            final int prize = parseInt(data.get(PRIZE_PREFIX + winnerCount), 0);
            System.out.println("  - " + winner + ": $" + prize);
            winnerCount++;
        }
    }

    private static void handleUpdateRound(Map<String, String> data, GameState gameState) {
        gameState.setGameMode(data.get(GAME_MODE));
        gameState.setGameModeRound(data.get(GAME_MODE_ROUND));
        gameState.setPot(parseInt(data.get(POT), 0));
        gameState.setNextPlayer(data.get(NEXT_PLAYER));
        gameState.setDealer(data.get(DEALER));
        final int playersLeft = getInt(data, PLAYERS_LEFT, 0);

        System.out.println("\n=== ACTUALIZACIÓN DE RONDA ===");
        System.out.println("Modo: " + gameState.getGameMode());
        System.out.println("Ronda: " + gameState.getGameModeRound());
        System.out.println("Pot: " + gameState.getPot());
        System.out.println("Dealer: " + gameState.getDealer());
        System.out.println("Siguiente: " + gameState.getNextPlayer());
        System.out.println("Jugadores restantes: " + playersLeft);
    }

    private static int getInt(Map<String, String> data, String key, int defaultValue) {
        final String s = data.get(key);
        if (s == null) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            System.err.println("Valor numérico inválido para clave '" + key + "': " + s);
            return defaultValue;
        }
    }
}
