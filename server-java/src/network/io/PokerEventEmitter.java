package network.io;

import network.ServerMessage;

import java.util.List;

public class PokerEventEmitter implements EventEmitter{
    @Override
    public void emit(List<Connection> connectionsList, String message) {
        for(Connection connection : connectionsList) {
            if (connection.isAlive()) {
                System.out.println(message);
                connection.addToOutputQueue(message);
            }
        }
    }
}
