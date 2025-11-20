package network.io;

import network.ServerMessage;

import java.util.List;

public class PokerEventEmitter implements EventEmitter{
    @Override
    public void emit(List<PlayerConnection> connectionsList, String message) {
        for(PlayerConnection connection : connectionsList) {
            if (connection.isAlive()) {
                connection.addToEventQueue(message);
            }
        }
    }
}
