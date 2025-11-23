package network.control;

import network.io.PlayerConnection;
import poker.items.PlayerModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SequenceConnectionPlayerMapping implements ConnectionPlayerMapping{
    private final Map<String, PlayerConnection> connectionMap;
    private final Map<String, PlayerModel> playerMap;

    private SequenceConnectionPlayerMapping(final Map<String, PlayerConnection> connectionMap,
                                            final Map<String, PlayerModel> playerMap) {
        this.connectionMap = connectionMap;
        this.playerMap = playerMap;
    }

    @Override
    public Optional<PlayerConnection> getConnectionFromName(final String name) {
        if(connectionMap.containsKey(name)) return Optional.of(connectionMap.get(name));
        return Optional.empty();
    }

    @Override
    public Optional<PlayerModel> getPlayerFromName(String name) {
        if(playerMap.containsKey(name)) return Optional.of(playerMap.get(name));
        return Optional.empty();
    }

    @Override
    public void deleteConnection(String name) {
        connectionMap.remove(name);
    }

    public static SequenceConnectionPlayerMapping build(List<PlayerConnection> connectionList, List<PlayerModel> player) {
        Map<String, PlayerConnection> map = new HashMap<>();
        Map<String, PlayerModel> map2 = new HashMap<>();
        return new SequenceConnectionPlayerMapping(map, map2);
    }



}
