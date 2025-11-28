package org.poker.connection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.poker.model.GameState;
import org.poker.model.PlayerModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public final class PokerClientTCP implements PokerClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private final String playerName;
    private final GameState gameState;
    private final Gson gson;
    private boolean connected = false;
    private MessageListener messageListener;

    public PokerClientTCP(String host, int port, String playerName) {
        this.playerName = playerName;
        this.gameState = new GameState();
        this.gson = new GsonBuilder().setPrettyPrinting().create();

        connect(host, port);
    }

    @Override
    public void joinGame(String gameMode) {
        Map<String, Object> message = new HashMap<>();
        message.put("event", "JOIN_GAME");
        message.put("game_mode", gameMode);
        message.put("player_name", playerName);

        sendMessage(message);
        System.out.println("Solicitando unirse al juego: " + gameMode);
    }

    @Override
    public void placeBet(String gameMode, int currentPlayer, String action, int bet) {
        Map<String, Object> message = new HashMap<>();
        message.put("event", "PLACE_BET");
        message.put("game_mode", gameMode);
        message.put("current_player", currentPlayer);
        message.put("player_action", action);
        message.put("bet", bet);

        sendMessage(message);
        System.out.println("Apuesta realizada: " + action + " " + bet);
    }

    @Override
    public void startListening() {
        new Thread(() -> {
            try {
                String line;
                while (connected && (line = in.readLine()) != null) {
                    handleServerMessage(line);
                }
            } catch (IOException e) {
                System.err.println("Error al leer del servidor: " + e.getMessage());
                connected = false;
            }
        }).start();
    }

    @Override
    public GameState getGameState() {
        return gameState;
    }

    @Override
    public boolean isMyTurn() {
        return gameState.nextPlayer.equals(playerName);
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
            if (socket != null) socket.close();
            if (in != null) in.close();
            if (out != null) out.close();
            System.out.println("Desconectado del servidor");
        } catch (IOException e) {
            System.err.println("Error al desconectar: " + e.getMessage());
        }
    }

    private void handleServerMessage(String json) {
        System.out.println("Recibido: " + json);

        try {
            JsonObject message = JsonParser.parseString(json).getAsJsonObject();
            String event = message.get("event").getAsString();

            switch (event) {
                case "GAME_STARTED":
                    handleGameStarted(message);
                    break;
                case "PLAYER_ACTION":
                    handlePlayerAction(message);
                    break;
                case "ROUND_OVER":
                    handleRoundOver(message);
                    break;
                case "GAME_ENDED":
                    handleGameEnded(message);
                    break;
                case "UPDATE_ROUND":
                    handleUpdateRound(message);
                    break;
                default:
                    System.out.println("Evento desconocido: " + event);
            }
        } catch (Exception e) {
            System.err.println("Error al procesar mensaje: " + e.getMessage());
        }
    }

    private void handleGameStarted(JsonObject message) {
        gameState.gameMode = message.get("game_mode").getAsString();
        gameState.nextPlayer = message.get("next_player").getAsString();
        gameState.dealer = message.get("dealer").getAsString();
        gameState.smallBlind = message.get("small_blind").getAsString();
        gameState.bigBlind = message.get("big_blind").getAsString();

        JsonArray players = message.getAsJsonArray("playerModels");
        gameState.players.clear();
        for (int i = 0; i < players.size(); i++) {
            JsonObject player = players.get(i).getAsJsonObject();
            PlayerModel pm = new PlayerModel();
            pm.name = player.get("player").getAsString();
            pm.index = player.get("index").getAsInt();
            gameState.players.add(pm);
        }

        System.out.println("\n=== JUEGO INICIADO ===");
        System.out.println("Modo: " + gameState.gameMode);
        System.out.println("Dealer: " + gameState.dealer);
        System.out.println("Small Blind: " + gameState.smallBlind);
        System.out.println("Big Blind: " + gameState.bigBlind);
        System.out.println("Jugadores: " + gameState.players.size());
        System.out.println("Siguiente: " + gameState.nextPlayer);
    }

    private void handlePlayerAction(JsonObject message) {
        String player = message.get("player").getAsString();
        String nextPlayer = message.get("next_player").getAsString();
        String action = message.get("pokerAction").getAsString();
        String dealer = message.get("dealer").getAsString();
        int pot = message.get("poker.pot").getAsInt();

        gameState.nextPlayer = nextPlayer;
        gameState.dealer = dealer;
        gameState.pot = pot;

        System.out.println("\n=== ACCIÓN DE JUGADOR ===");
        System.out.println("Jugador: " + player + " -> " + action);
        System.out.println("Pot: " + pot);
        System.out.println("Siguiente: " + nextPlayer);
    }

    private void handleRoundOver(JsonObject message) {
        String gameMode = message.get("game_mode").getAsString();
        String nextRound = message.get("next_round").getAsString();
        int pot = message.get("poker.pot").getAsInt();

        gameState.pot = pot;

        System.out.println("\n=== RONDA TERMINADA ===");
        System.out.println("Modo: " + gameMode);
        System.out.println("Siguiente ronda: " + nextRound);
        System.out.println("Pot: " + pot);
    }

    private void handleGameEnded(JsonObject message) {
        String gameMode = message.get("game_mode").getAsString();
        JsonArray winners = message.getAsJsonArray("winners");

        System.out.println("\n=== JUEGO TERMINADO ===");
        System.out.println("Modo: " + gameMode);
        System.out.println("Ganadores:");

        for (int i = 0; i < winners.size(); i++) {
            JsonObject winner = winners.get(i).getAsJsonObject();
            String player = winner.get("player").getAsString();
            int prize = winner.get("poker.pot").getAsInt();
            System.out.println("  - " + player + ": $" + prize);
        }
    }

    private void handleUpdateRound(JsonObject message) {
        gameState.gameMode = message.get("gamemode").getAsString();
        gameState.gameModeRound = message.get("gamemode_round").getAsString();
        gameState.pot = message.get("pot").getAsInt();
        gameState.nextPlayer = message.get("next_player").getAsString();
        gameState.dealer = message.get("dealer").getAsString();
        int playersLeft = message.get("players_left").getAsInt();

        System.out.println("\n=== ACTUALIZACIÓN DE RONDA ===");
        System.out.println("Modo: " + gameState.gameMode);
        System.out.println("Ronda: " + gameState.gameModeRound);
        System.out.println("Pot: " + gameState.pot);
        System.out.println("Dealer: " + gameState.dealer);
        System.out.println("Siguiente: " + gameState.nextPlayer);
        System.out.println("Jugadores restantes: " + playersLeft);
    }

    private void connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            connected = true;
            System.out.println("Conectado al servidor en " + host + ":" + port);
        } catch (IOException e) {
            throw new RuntimeException("Error al conectar: " + e.getMessage());
        }
    }

    private void sendMessage(Map<String, Object> message) {
        if (!connected) {
            System.err.println("No conectado al servidor");
            return;
        }

        String json = gson.toJson(message);
        out.println(json);
        System.out.println("Enviado: " + json);
    }
}
