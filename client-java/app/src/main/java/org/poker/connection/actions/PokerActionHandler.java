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
    private static final String GAME_MODE = "gamemode";
    private static final String TURN = "turn";
    private static final String DEALER = "dealer";
    private static final String SMALL_BLIND = "small_blind";
    private static final String BIG_BLIND = "big_blind";
    private static final String POT = "pot";
    private static final String PLAYERS_LEFT = "players_left";
    private static final String GAME_MODE_ROUND = "gamemode_round";
    private static final String LAST_RAISE = "last_raise";
    private static final String WINNER = "winner";

    private static final String EVENT_GAME_STARTED = "game_started";
    private static final String EVENT_PLAYER_ACTION = "player_action";
    private static final String EVENT_ROUND_OVER = "round_over";
    private static final String EVENT_GAME_OVER = "game_over";
    private static final String EVENT_ROUND_UPDATE = "round_update";

    private PokerActionHandler() {

    }

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
            case EVENT_GAME_STARTED:
                handleGameStarted(data, gameState);
                break;
            case EVENT_PLAYER_ACTION:
                handlePlayerAction(data, gameState);
                break;
            case EVENT_ROUND_OVER:
                handleRoundOver(data, gameState);
                break;
            case EVENT_GAME_OVER:
                handleGameEnded(data);
                break;
            case EVENT_ROUND_UPDATE:
                handleUpdateRound(data, gameState);
                break;
            default:
                System.out.println("Evento desconocido: " + event);
        }
    }

    private static void handleGameStarted(Map<String, String> data, GameState gameState) {
        gameState.setGameMode(data.get(GAME_MODE));
        gameState.setPot(getInt(data, POT, 0));

        gameState.playersClear();
        int playerCount = MessageParser.getPlayerCount(data);

        for (int i = 0; i < playerCount; i++) {
            String playerName = MessageParser.getPlayerName(data, i);
            String cards = MessageParser.getPlayerCards(data, playerName);
            String money = MessageParser.getPlayerMoney(data, playerName);

            PlayerModel pm = new PlayerModel();
            pm.setName(playerName);
            pm.setIndex(i);
            pm.setMoney(getInt(money));
            pm.setCardsFromString(cards);

            gameState.playersAdd(pm);
        }

        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║         JUEGO INICIADO             ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║ Modo: " + pad(gameState.getGameMode(), 28) + " ║");
        System.out.println("║ Pot: $" + pad(String.valueOf(gameState.getPot()), 27) + " ║");
        System.out.println("║ Jugadores: " + pad(String.valueOf(playerCount), 23) + " ║");
        System.out.println("╚════════════════════════════════════╝");

        for (PlayerModel player : gameState.getPlayers()) {
            System.out.println("  • " + player.getName() + " - $" + player.getMoney() +
                    " - Cartas: " + player.getCardsString());
        }
    }

    private static void handlePlayerAction(Map<String, String> data, GameState gameState) {
        final String player = data.get("player");
        final String action = data.get("action");
        final String turn = data.get(TURN);
        final int pot = getInt(data, POT, gameState.getPot());

        gameState.setNextPlayer(turn);
        gameState.setPot(pot);

        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║        ACCIÓN DE JUGADOR           ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║ Jugador: " + pad(player, 25) + " ║");
        System.out.println("║ Acción: " + pad(action, 26) + " ║");
        System.out.println("║ Pot: $" + pad(String.valueOf(pot), 27) + " ║");
        System.out.println("║ Siguiente: " + pad(turn, 23) + " ║");
        System.out.println("╚════════════════════════════════════╝");
    }

    private static void handleRoundOver(Map<String, String> data, GameState gameState) {
        final String gameMode = data.get(GAME_MODE);
        final int pot = getInt(data, POT, gameState.getPot());

        gameState.setPot(pot);

        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║         RONDA TERMINADA            ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║ Modo: " + pad(gameMode, 28) + " ║");
        System.out.println("║ Pot: $" + pad(String.valueOf(pot), 27) + " ║");
        System.out.println("╚════════════════════════════════════╝");
    }

    private static void handleGameEnded(Map<String, String> data) {
        final String winner = data.get(WINNER);
        final int playersLeft = getInt(data, PLAYERS_LEFT, 0);

        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║         JUEGO TERMINADO            ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║ Ganador: " + pad(winner, 25) + " ║");
        System.out.println("║ Jugadores restantes: " + pad(String.valueOf(playersLeft), 13) + " ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║         DINERO FINAL               ║");
        System.out.println("╠════════════════════════════════════╣");

        int playerCount = MessageParser.getPlayerCount(data);
        for (int i = 0; i < playerCount; i++) {
            String playerName = MessageParser.getPlayerName(data, i);
            String money = MessageParser.getPlayerMoney(data, playerName);
            System.out.println("║ " + pad(playerName + ": $" + money, 35) + " ║");
        }

        System.out.println("╚════════════════════════════════════╝");
    }

    private static void handleUpdateRound(Map<String, String> data, GameState gameState) {
        gameState.setGameMode(data.get(GAME_MODE));
        gameState.setGameModeRound(data.get(GAME_MODE_ROUND));
        gameState.setPot(getInt(data, POT, 0));
        gameState.setNextPlayer(data.get(TURN));
        gameState.setDealer(data.get(DEALER));
        gameState.setSmallBlind(data.get(SMALL_BLIND));
        gameState.setBigBlind(data.get(BIG_BLIND));

        final int playersLeft = getInt(data, PLAYERS_LEFT, 0);
        final int lastRaise = getInt(data, LAST_RAISE, 0);

        gameState.playersClear();
        int playerCount = MessageParser.getPlayerCount(data);

        for (int i = 0; i < playerCount; i++) {
            String playerName = MessageParser.getPlayerName(data, i);
            String cards = MessageParser.getPlayerCards(data, playerName);
            String money = MessageParser.getPlayerMoney(data, playerName);

            PlayerModel pm = new PlayerModel();
            pm.setName(playerName);
            pm.setIndex(i);
            pm.setMoney(getInt(money));
            pm.setCardsFromString(cards);

            gameState.playersAdd(pm);
        }

        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║      ACTUALIZACIÓN DE RONDA        ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║ Modo: " + pad(gameState.getGameMode(), 28) + " ║");
        System.out.println("║ Ronda: " + pad(gameState.getGameModeRound(), 27) + " ║");
        System.out.println("║ Pot: $" + pad(String.valueOf(gameState.getPot()), 27) + " ║");
        System.out.println("║ Última subida: $" + pad(String.valueOf(lastRaise), 19) + " ║");
        System.out.println("║ Dealer: " + pad(gameState.getDealer(), 26) + " ║");
        System.out.println("║ Small Blind: " + pad(gameState.getSmallBlind(), 21) + " ║");
        System.out.println("║ Big Blind: " + pad(gameState.getBigBlind(), 23) + " ║");
        System.out.println("║ Turno: " + pad(gameState.getNextPlayer(), 27) + " ║");
        System.out.println("║ Jugadores: " + pad(String.valueOf(playersLeft), 23) + " ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║           JUGADORES                ║");
        System.out.println("╠════════════════════════════════════╣");

        for (PlayerModel player : gameState.getPlayers()) {
            String playerInfo = player.getName() + " $" + player.getMoney();
            System.out.println("║ " + pad(playerInfo, 35) + " ║");
            player.getCardsString();
            if (!player.getCardsString().isEmpty()) {
                System.out.println("║   Cartas: " + pad(player.getCardsString(), 26) + " ║");
            }
        }

        System.out.println("╚════════════════════════════════════╝");
    }

    private static int getInt(Map<String, String> data, String key, int defaultValue) {
        final String s = data.get(key);
        return getInt(s, defaultValue);
    }

    private static int getInt(String s) {
        return getInt(s, 0);
    }

    private static int getInt(String s, int defaultValue) {
        if (s == null || s.isEmpty()) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            System.err.println("Valor numérico inválido: " + s);
            return defaultValue;
        }
    }

    private static String pad(String s, int length) {
        if (s == null) s = "N/A";
        if (s.length() >= length) return s.substring(0, length);
        return String.format("%-" + length + "s", s);
    }
}
