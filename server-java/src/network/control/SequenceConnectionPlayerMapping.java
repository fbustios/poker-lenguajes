package network.control;

import network.io.Connection;
import poker.items.Player;

import java.util.*;

public class SequenceConnectionPlayerMapping implements ConnectionPlayerMapping{
    private final Map<String, Connection> connectionMap;
    private final Map<String, Player> playerMap;

    private SequenceConnectionPlayerMapping(final Map<String, Connection> connectionMap,
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

    public static SequenceConnectionPlayerMapping build(Map<Connection, Player> mapping) {
        Map<String, Connection> connectionMap2 = new HashMap<>();
        Map<String, Player> playerMap2 = new HashMap<>();
        for (Map.Entry<Connection, Player> entry : mapping.entrySet()) {
            connectionMap2.put(entry.getValue().getName(), entry.getKey());
            connectionMap2.put(entry.getValue().getName(), entry.getKey());
        }
        return new SequenceConnectionPlayerMapping(connectionMap2, playerMap2);
    }



}
