package org.poker.connection;

import org.poker.connection.messages.MessageListener;
import org.poker.model.GameState;

public interface PokerClient {
    void joinGame(String gameMode);
    void leaveGame();
    void placeBet(String gameMode, int currentPlayer, String action, int bet);
    void startListening();
    GameState getGameState();
    String getPlayerName();
    boolean isMyTurn();
    void disconnect();
    boolean isConnected();
    void setMessageListener(MessageListener listener);
}
