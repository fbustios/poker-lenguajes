package network.control;

import network.io.Connection;
import poker.items.Player;

import java.util.*;

public class MapPlayerMapping implements ConnectionPlayerMapping{
    private final Map<String, Connection> connectionMap;
    private final Map<String, Player> playerMap;

    private MapPlayerMapping(final Map<String, Connection> connectionMap,
                             final Map<String, Player> playerMap) {
        this.connectionMap = connectionMap;
        this.playerMap = playerMap;
    }

    @Override
    public Optional<Connection> getConnectionFromName(final String name) {
        if(connectionMap.containsKey(name)) return Optional.of(connectionMap.get(name));
        return Optional.empty();
    }

    @Override
    public Optional<Player> getPlayerFromName(String name) {
        if(playerMap.containsKey(name)) return Optional.of(playerMap.get(name));
        return Optional.empty();
    }

    @Override
    public void deleteConnection(String name) {
        connectionMap.remove(name);
    }

    @Override
    public List<Connection> getConnections() {
        return new ArrayList<>(connectionMap.values());
    }
    @Override
    public List<Player> getPlayers() {
        return new ArrayList<>(playerMap.values());
    }

    public static MapPlayerMapping build(Map<Connection, Player> mapping) {
        Map<String, Connection> connectionMap2 = new HashMap<>();
        Map<String, Player> playerMap2 = new HashMap<>();
        for (Map.Entry<Connection, Player> entry : mapping.entrySet()) {
            connectionMap2.put(entry.getValue().getName(), entry.getKey());
            playerMap2.put(entry.getValue().getName(), entry.getValue());
        }
        System.out.println(connectionMap2.values().size());
        System.out.println(playerMap2.values().size());

        return new MapPlayerMapping(connectionMap2, playerMap2);
    }

}
