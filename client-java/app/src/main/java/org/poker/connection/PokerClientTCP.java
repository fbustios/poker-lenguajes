package org.poker.connection;

import org.poker.connection.actions.PokerActionHandler;
import org.poker.connection.messages.MessageHandler;
import org.poker.connection.messages.MessageHandlerText;
import org.poker.connection.messages.MessageListener;
import org.poker.model.GameState;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class PokerClientTCP implements PokerClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private final GameState gameState;
    private boolean connected = false;
    private MessageListener messageListener;

    private MessageHandler messageHandler;

    public PokerClientTCP(final String host, final int port) {
        this.gameState = new GameState();
        connect(host, port);
    }

    @Override
    public void joinGame(String gameMode, String playerName, int money) {
        final Map<String, String> message = new HashMap<>();
        message.put("event", "join_game");
        message.put("game_mode", gameMode);
        message.put("player_name", playerName);
        message.put("money", String.valueOf(money));

        if (connected) {
            messageHandler.sendMessage(message);
            System.out.println("Solicitando unirse al juego: " + gameMode);
        } else {
            System.err.println("No conectado al servidor");
        }
    }

    @Override
    public void leaveGame(String playerName) {
        final Map<String, String> message = new HashMap<>();
        message.put("event", "leave_game");
        message.put("player_name", playerName);

        messageHandler.sendMessage(message);
        System.out.println("Saliendo del juego...");
    }

    @Override
    public void placeBet(String gameMode, String playerName, String action, int bet) {
        final Map<String, String> message = new HashMap<>();
        message.put("event", "place_bet");
        message.put("game_mode", gameMode);
        message.put("current_player", playerName);
        message.put("player_action", action);
        message.put("bet", String.valueOf(bet));

        if (connected) {
            messageHandler.sendMessage(message);
            System.out.println("Apuesta realizada: " + action + " " + bet);
        } else {
            System.err.println("No conectado al servidor");
        }
    }

    @Override
    public void startListening() {
        new Thread(() -> {
            while (connected) {
                final Optional<String> message = messageHandler.receiveMessage();
                message.ifPresent(s -> PokerActionHandler.handleServerMessage(s, gameState, messageListener));
            }
        }).start();
    }

    @Override
    public GameState getGameState() {
        return gameState;
    }

    @Override
    public boolean isMyTurn(String playerName) {
        return gameState.getNextPlayer() != null && gameState.getNextPlayer().equals(playerName);
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void setMessageListener(MessageListener listener) {
        this.messageListener = listener;
    }

    @Override
    public void disconnect() {
        try {
            connected = false;
            if (socket != null) {
                socket.close();
            }
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            System.out.println("Desconectado del servidor");
        } catch (IOException e) {
            System.err.println("Error al desconectar: " + e.getMessage());
        }
    }

    private void connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            this.messageHandler = new MessageHandlerText(in, out);

            connected = true;
            System.out.println("Conectado al servidor en " + host + ":" + port);
        } catch (IOException e) {
            throw new RuntimeException("Error al conectar: " + e.getMessage());
        }
    }
}
