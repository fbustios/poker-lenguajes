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
    private static final String WINNER_PREFIX = "winner_";
    private static final String PRIZE_PREFIX = "prize_";
    private static final String PLAYERS_LEFT = "players_left";
    private static final String GAME_MODE_ROUND = "game_mode_round";
    private static final String NEXT_ROUND = "next_round";
    private static final String TURN = "turn";
    private static final String LAST_RAISE = "last_raise";
    private static final String EVENT_GAME_STARTED = "game_started";
    private static final String EVENT_PLAYER_ACTION = "player_action";
    private static final String EVENT_ROUND_OVER = "round_over";
    private static final String EVENT_GAME_ENDED = "game_ended";
    private static final String EVENT_ROUND_UPDATE = "update_round";
    private static final String COMMUNITY_CARDS = "community_cards";
    private static final String MODE_CHANGED = "mode_changed";

    private
    
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
            case EVENT_GAME_STARTED:
                handleGameStarted(data, gameState);
                break;
            case EVENT_PLAYER_ACTION:
                handlePlayerAction(data, gameState);
                break;
            case EVENT_ROUND_OVER:
                handleRoundOver(data, gameState);
                break;
            case EVENT_GAME_ENDED:
                handleGameEnded(data);
                break;
            case EVENT_ROUND_UPDATE:
                handleUpdateRound(data, gameState);
                break;
            case MODE_CHANGED:
                handleModeChange(data, gameState);
            default:
                System.out.println("Evento desconocido: " + event);
        }
        if (messageListener != null) {
            messageListener.onMessageReceived(event.get(), data);
        }
    }

    private static void handleModeChange(Map<String, String> data, GameState gameState) {
        gameState.setGameMode(data.get(GAME_MODE));

        final int playersLeft = getInt(data, PLAYERS_LEFT, 0);
        gameState.playersClear();
        for (int i = 0; i < playersLeft; i++) {
            String playerName = MessageParser.getPlayerName(data, i);
            String cards = MessageParser.getPlayerCards(data, playerName);
            String money = MessageParser.getPlayerMoney(data, playerName);

            PlayerModel pm = new PlayerModel();
            pm.setName(playerName);
            pm.setMoney(getInt(money));
            pm.setCardsFromString(cards);

            gameState.playersAdd(pm);
        }

        for (PlayerModel player : gameState.getPlayers()) {
            System.out.println("  • " + player.getName() + " - $" + player.getMoney() +
                    " - Cartas: " + player.getCardsString());
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
        final int playersLeft = getInt(data, PLAYERS_LEFT, 0);

        gameState.playersClear();
        for (int i = 0; i < playersLeft; i++) {
            String playerName = MessageParser.getPlayerName(data, i);
            String cards = MessageParser.getPlayerCards(data, playerName);
            String money = MessageParser.getPlayerMoney(data, playerName);

            PlayerModel pm = new PlayerModel();
            pm.setName(playerName);
            pm.setMoney(getInt(money));
            pm.setCardsFromString(cards);

            gameState.playersAdd(pm);
        }

        for (PlayerModel player : gameState.getPlayers()) {
            System.out.println("  • " + player.getName() + " - $" + player.getMoney() +
                    " - Cartas: " + player.getCardsString());
        }
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
        gameState.setPot(getInt(data, POT, 0));
        gameState.setNextPlayer(data.get(TURN));
        gameState.setDealer(data.get(DEALER));
        gameState.setSmallBlind(data.get(SMALL_BLIND));
        gameState.setBigBlind(data.get(BIG_BLIND));
        gameState.setNextPlayer(data.get(NEXT_PLAYER));
        gameState.setLast_raise(getInt(data, LAST_RAISE, 0));
        gameState.setCommunityCardsFromString(data.get(COMMUNITY_CARDS));

        final int playersLeft = getInt(data, PLAYERS_LEFT, 0);

        gameState.playersClear();
        for (int i = 0; i < playersLeft; i++) {
            String playerName = MessageParser.getPlayerName(data, i);
            String cards = MessageParser.getPlayerCards(data, playerName);
            String money = MessageParser.getPlayerMoney(data, playerName);

            PlayerModel pm = new PlayerModel();
            pm.setName(playerName);
            pm.setMoney(getInt(money));
            pm.setCardsFromString(cards);

            gameState.playersAdd(pm);
        }
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
            System.err.println("Valor numerico invalido: " + s);
            return defaultValue;
        }
    }
}
