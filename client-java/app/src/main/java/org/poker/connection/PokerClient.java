package org.poker.connection;

import org.poker.connection.messages.MessageListener;
import org.poker.model.GameState;

public interface PokerClient {
    void joinGame(String gameMode, String playerName, int money);
    void leaveGame(String playerName);
    void placeBet(String gameMode, String playerName, String action, int bet);
    void startListening();
    GameState getGameState();
    boolean isMyTurn(String playerName);
    void disconnect();
    boolean isConnected();
    void setMessageListener(MessageListener listener);
}
