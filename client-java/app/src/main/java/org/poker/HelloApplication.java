package org.poker;

import org.poker.connection.GameMode;
import org.poker.connection.PokerClient;
import org.poker.ui.GameDisplayController;
import org.poker.ui.LoginDisplay;

import javax.swing.*;

public class HelloApplication {
    private static LoginDisplay loginDisplay;

    private static GameDisplayController gameController;

    private static PokerClient activeClient;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                showLogin();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void showLogin() {
        if (loginDisplay == null) {
            loginDisplay = new LoginDisplay();
            loginDisplay.setOnConnectAction(client -> {
                activeClient = client;
                System.out.println("Cliente recibido en Main: " + client.getPlayerName());
            });

            loginDisplay.setJoinGameAction(() -> {
                if (activeClient != null && activeClient.isConnected()) {
                    showGame();
                    loginDisplay.setVisible(false);
                } else {
                    System.err.println("Error: Intentando iniciar juego sin cliente conectado.");
                }
            });

            loginDisplay.setVisible(true);
        } else {
            loginDisplay.setVisible(true);
        }
    }

    public static void showGame() {
        if (gameController == null) {
            gameController = new GameDisplayController(GameMode.HOLDEM, activeClient);
        }

    }
}