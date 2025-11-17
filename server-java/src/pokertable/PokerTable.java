package pokertable;

import pokeritems.PlayerModel;

import java.util.List;

public interface PokerTable {
    PlayerModel next();
    List<PlayerModel> getPlayers();
    void resetTable();
}
