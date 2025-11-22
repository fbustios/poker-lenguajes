package network.control;

import network.io.PlayerConnection;
import poker.items.PlayerModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SequenceConnectionPlayerMapping implements ConnectionPlayerMapping{
    private final Map<String, PlayerConnection> connectionMap;

    private SequenceConnectionPlayerMapping(final Map<String, PlayerConnection> connectionMap) {
        this.connectionMap = connectionMap;
    }

    @Override
    public Optional<PlayerConnection> getConnectionFromName(final String name) {
        if(connectionMap.containsKey(name)) return Optional.of(connectionMap.get(name));
        return Optional.empty();
    }

    @Override
    public void deleteConnection(String name) {
        connectionMap.remove(name);
    }

    public static SequenceConnectionPlayerMapping build(List<PlayerConnection> connectionList) {
        Map<String, PlayerConnection> map = new HashMap<>();
        return new SequenceConnectionPlayerMapping(map);
    }



}
