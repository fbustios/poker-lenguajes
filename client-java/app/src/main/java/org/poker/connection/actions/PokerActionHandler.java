package org.poker.connection.actions;

import org.poker.connection.MessageParser;
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

    public static void handleServerMessage(String message, GameState gameState, MessageListener messageListener) {
        final Map<String, String> data = MessageParser.parse(message);
        final Optional<String> event = Optional.ofNullable(data.get(EVENT));

        if (event.isEmpty()) {
            System.err.println("Mensaje sin evento");
            return;
        }

        if (messageListener != null) {
            messageListener.onMessageReceived(event.get(), data);
        }

        switch (event.get()) {
            case "GAME_STARTED":
                handleGameStarted(data, gameState);
                break;
            case "PLAYER_ACTION":
                handlePlayerAction(data, gameState);
                break;
            case "ROUND_OVER":
                handleRoundOver(data, gameState);
                break;
            case "GAME_ENDED":
                handleGameEnded(data);
                break;
            case "UPDATE_ROUND":
                handleUpdateRound(data, gameState);
                break;
            default:
                System.out.println("Evento desconocido: " + event);
        }
    }

    private static void handleGameStarted(Map<String, String> data, GameState gameState) {
        gameState.gameMode = data.get(GAME_MODE);
        gameState.nextPlayer = data.get(NEXT_PLAYER);
        gameState.dealer = data.get(DEALER);
        gameState.smallBlind = data.get(SMALL_BLIND);
        gameState.bigBlind = data.get(BIG_BLIND);

        gameState.players.clear();
        int playerCount = 0;
        while (data.containsKey(PLAYER_PREFIX + playerCount)) {
            PlayerModel pm = new PlayerModel();
            pm.name = data.get(PLAYER_PREFIX + playerCount);
            pm.index = playerCount;
            gameState.players.add(pm);
            playerCount++;
        }

        System.out.println("\n=== JUEGO INICIADO ===");
        System.out.println("Modo: " + gameState.gameMode);
        System.out.println("Dealer: " + gameState.dealer);
        System.out.println("Small Blind: " + gameState.smallBlind);
        System.out.println("Big Blind: " + gameState.bigBlind);
        System.out.println("Jugadores: " + gameState.players.size());
        System.out.println("Siguiente: " + gameState.nextPlayer);
    }

    private static void handlePlayerAction(Map<String, String> data, GameState gameState) {
        String player = data.get("player");
        String nextPlayer = data.get(NEXT_PLAYER);
        String action = data.get("action");
        String dealer = data.get(DEALER);
        int pot = getInt(data, POT, gameState.pot);

        gameState.nextPlayer = nextPlayer;
        gameState.dealer = dealer;
        gameState.pot = pot;

        System.out.println("\n=== ACCIÓN DE JUGADOR ===");
        System.out.println("Jugador: " + player + " -> " + action);
        System.out.println("Pot: " + pot);
        System.out.println("Siguiente: " + nextPlayer);
    }

    private static void handleRoundOver(Map<String, String> data, GameState gameState) {
        String gameMode = data.get(GAME_MODE);
        String nextRound = data.get(NEXT_ROUND);
        int pot = getInt(data, POT, gameState.pot);

        gameState.pot = pot;

        System.out.println("\n=== RONDA TERMINADA ===");
        System.out.println("Modo: " + gameMode);
        System.out.println("Siguiente ronda: " + nextRound);
        System.out.println("Pot: " + pot);
    }

    private static void handleGameEnded(Map<String, String> data) {
        String gameMode = data.get(GAME_MODE);

        System.out.println("\n=== JUEGO TERMINADO ===");
        System.out.println("Modo: " + gameMode);
        System.out.println("Ganadores:");

        int winnerCount = 0;
        while (data.containsKey(WINNER_PREFIX + winnerCount)) {
            String winner = data.get(WINNER_PREFIX + winnerCount);
            int prize = parseInt(data.get(PRIZE_PREFIX + winnerCount), 0);
            System.out.println("  - " + winner + ": $" + prize);
            winnerCount++;
        }
    }

    private static void handleUpdateRound(Map<String, String> data, GameState gameState) {
        gameState.gameMode = data.get(GAME_MODE);
        gameState.gameModeRound = data.get(GAME_MODE_ROUND);
        gameState.pot = parseInt(data.get(POT), 0);
        gameState.nextPlayer = data.get(NEXT_PLAYER);
        gameState.dealer = data.get(DEALER);
        int playersLeft = getInt(data, PLAYERS_LEFT,0);

        System.out.println("\n=== ACTUALIZACIÓN DE RONDA ===");
        System.out.println("Modo: " + gameState.gameMode);
        System.out.println("Ronda: " + gameState.gameModeRound);
        System.out.println("Pot: " + gameState.pot);
        System.out.println("Dealer: " + gameState.dealer);
        System.out.println("Siguiente: " + gameState.nextPlayer);
        System.out.println("Jugadores restantes: " + playersLeft);
    }

    private static int getInt(Map<String, String> data, String key, int defaultValue) {
        String s = data.get(key);
        if (s == null) return defaultValue;
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            System.err.println("Valor numérico inválido para clave '" + key + "': " + s);
            return defaultValue;
        }
    }
}
