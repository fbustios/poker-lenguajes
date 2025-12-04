package org.poker.ui;

import org.poker.connection.GameMode;
import org.poker.connection.PokerClient;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class GameDisplayController {
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
    private final PokerClient client;
    private GameMode currentMode;
    private final Map<GameMode, ModeDisplay> modeDisplays;
    private String localPlayerName;

    public GameDisplayController(GameMode startingMode, PokerClient client) {
        this.currentMode = startingMode;
        this.client = client;
        this.modeDisplays = new HashMap<>();

        if (client != null) {
            this.localPlayerName = client.getPlayerName();
        } else {
            this.localPlayerName = "Unknown";
            System.err.println("Advertencia: Cliente nulo o sin nombre en Controller.");
        }

        initializeModeDisplays();
        setupServerListener();

        // Iniciar en el modo seleccionado por defecto
        changeMode(startingMode);
    }

    private void initializeModeDisplays() {
        // Inicializamos las pantallas de cada modo
        modeDisplays.put(GameMode.HOLDEM, new HoldEmModeDisplay(client));
        modeDisplays.put(GameMode.OMAHA, new OmahaHIModeDisplay(client));
        modeDisplays.put(GameMode.RAZZ, new RazzModeDisplay(client));
        modeDisplays.put(GameMode.STUD, new SevenCardStudModeDisplay(client));
        modeDisplays.put(GameMode.EIGHT, new OmahaHiLoModeDisplay(client));

        // Propagar el nombre del jugador a todas las vistas
        for (ModeDisplay display : modeDisplays.values()) {
            display.setLocalPlayerName(localPlayerName);
        }
    }

    private void setupServerListener() {
        if (client == null) return;

        client.setMessageListener((event, message) -> {
            SwingUtilities.invokeLater(() -> {

                switch (event) {
                    case "game_started":
                        if (message.containsKey("game_mode")) {
                            handleModeChange(message.get("game_mode"));
                        }
                        refreshCurrentDisplay(message);
                        break;

                    case "update_round":
                        if (message.containsKey("game_mode")) {
                            handleModeChange(message.get("game_mode"));
                        }
                        refreshCurrentDisplay(message);
                        break;

                    case "player_action":
                        refreshCurrentDisplay(message);
                        break;

                    case "round_over":
                        refreshCurrentDisplay(message);
                        break;

                    case "game_ended":
                        handleGameEnded(message);
                        refreshCurrentDisplay(message);
                        break;

                    case "game_state_update":
                        refreshCurrentDisplay(message);
                        break;

                    default:
                        refreshCurrentDisplay(message);
                        break;
                }
            });
        });
    }

    private void refreshCurrentDisplay(Map<String, String> message) {
        if (currentMode != null && modeDisplays.containsKey(currentMode)) {
            modeDisplays.get(currentMode).updateState(message);
        }
    }

    private void handleGameEnded(Map<String, String> message) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ¡MANO FINALIZADA! ===\n\n");

        int i = 0;
        boolean foundWinners = false;
        while (message.containsKey("winner_" + i)) {
            String winner = message.get("winner_" + i);
            String prize = message.getOrDefault("prize_" + i, "0");

            sb.append("🏆 ").append(winner)
                    .append(" gana $").append(prize).append("\n");

            i++;
            foundWinners = true;
        }

        if (foundWinners) {
            JOptionPane.showMessageDialog(null, sb.toString(), "Ganadores", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void handleModeChange(String modeStr) {
        try {
            if (modeStr == null) return;

            GameMode newMode = GameMode.valueOf(modeStr.toUpperCase());

            if (newMode != currentMode) {
                System.out.println("Cambiando modo visual a: " + newMode);
                changeMode(newMode);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Modo desconocido recibido: " + modeStr);
        }
    }

    private void changeMode(GameMode newMode) {
        if (currentMode != null && modeDisplays.containsKey(currentMode)) {
            modeDisplays.get(currentMode).hideDisplay();
        }

        this.currentMode = newMode;

        if (modeDisplays.containsKey(newMode)) {
            ModeDisplay display = modeDisplays.get(newMode);
            display.showDisplay();
            display.updateState(new HashMap<>());
        } else {
            System.err.println("No hay implementación de UI para el modo: " + newMode);
        }
    }
}