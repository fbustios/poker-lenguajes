package network.control;

import network.io.Connection;
import poker.items.Player;

import java.util.List;
import java.util.Optional;

public interface ConnectionPlayerMapping {
    Optional<Connection> getConnectionFromName(String name);
    Optional<Player> getPlayerFromName(String name);
    void deleteConnection(String name);
    List<Connection> getConnections();
}
