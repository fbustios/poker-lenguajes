package network.control;

import network.io.Connection;
import poker.items.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public static SequenceConnectionPlayerMapping build(List<Connection> connectionList, List<Player> player) {
        Map<String, Connection> map = new HashMap<>();
        Map<String, Player> map2 = new HashMap<>();
        return new SequenceConnectionPlayerMapping(map, map2);
    }



}
