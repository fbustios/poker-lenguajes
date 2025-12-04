package org.poker.connection;

import org.poker.connection.actions.PokerActionHandler;
import org.poker.connection.messages.MessageHandler;
import org.poker.connection.messages.MessageHandlerText;
import org.poker.connection.messages.MessageListener;
import org.poker.model.GameState;


import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public final class PokerClientTCP implements PokerClient {
    private static final String MSG_JOIN_REQUEST = "Solicitando unirse al juego: ";
    private static final String MSG_EXIT_GAME = "Saliendo del juego...";
    private static final String MSG_ERROR_NOT_CONNECTED = "No conectado al servidor";
    private static final String MSG_ERROR_DISCONNECT = "Error al desconectar: ";

    private static final String EVENT_KEY = "event";
    private static final String PLAYER_NAME_KEY = "player_name";
    private static final String AUTHOR_KEY = "author";
    private static final String PLAYER_ACTION_KEY = "player_action";
    private static final String BET_KEY = "bet";
    private static final String EVENT_JOIN_GAME_VALUE = "join_game";
    private static final String EVENT_LEAVE_GAME_VALUE = "leave_game";
    private static final String EVENT_ACTION = "action";

    private Socket socket;
    private DataInputStream in;
    private OutputStream out;
    private final String playerName;
    private final GameState gameState;
    private boolean connected = false;
    private MessageListener messageListener;

    private MessageHandler messageHandler;

    public PokerClientTCP(final String host, final int port, final String playerName) {
        this.playerName = playerName;
        this.gameState = new GameState();
        connect(host, port);
    }

    @Override
    public void joinGame(String gameMode, int bet) {
        final Map<String, String> message = new LinkedHashMap<>();
        message.put(EVENT_KEY, EVENT_JOIN_GAME_VALUE);
        message.put(PLAYER_NAME_KEY, playerName);
        message.put(BET_KEY, String.valueOf(bet));

        if (connected) {
            messageHandler.sendMessage(message);
            System.out.println(MSG_JOIN_REQUEST + gameMode);
        } else {
            System.err.println(MSG_ERROR_NOT_CONNECTED);
        }

    }

    @Override
    public void leaveGame() {
        final Map<String, String> message = new LinkedHashMap<>();
        message.put(EVENT_KEY, EVENT_LEAVE_GAME_VALUE);
        message.put(PLAYER_NAME_KEY, playerName);

        messageHandler.sendMessage(message);
        System.out.println(MSG_EXIT_GAME);
    }

    @Override
    public void placeBet(String gameMode, int currentPlayer, String action, int bet) {
        final Map<String, String> message = new LinkedHashMap<>();
        message.put(EVENT_KEY, EVENT_ACTION);
        message.put(AUTHOR_KEY, playerName);
        message.put(PLAYER_ACTION_KEY, action);
        message.put(BET_KEY, String.valueOf(bet));

        if (connected) {
            messageHandler.sendMessage(message);
            System.out.println("Apuesta realizada: " + action + " " + bet);
        } else {
            System.err.println(MSG_ERROR_NOT_CONNECTED);
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
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public GameState getGameState() {
        return gameState;
    }

    @Override
    public boolean isMyTurn() {
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
            System.err.println(MSG_ERROR_DISCONNECT + e.getMessage());
        }
    }

    private void connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            in = new DataInputStream(socket.getInputStream());
            out = socket.getOutputStream();
            this.messageHandler = new MessageHandlerText(in, out);

            connected = true;
            System.out.println("Conectado al servidor en " + host + ":" + port);
        } catch (IOException e) {
            throw new RuntimeException("Error al conectar: " + e.getMessage());
        }
    }
}
