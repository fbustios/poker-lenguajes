package network.control;

import network.io.PlayerConnection;
import poker.items.PlayerModel;

import java.util.Optional;

public interface ConnectionPlayerMapping {
    Optional<PlayerConnection> getConnectionFromName(String name);
    Optional<PlayerModel> getPlayerFromName(String name);
    void deleteConnection(String name);
}
