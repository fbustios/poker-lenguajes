package poker.table;

import poker.items.PlayerModel;

import java.util.List;

public interface PokerTable {
    PlayerModel next();
    List<PlayerModel> getPlayers();
    List<PlayerModel> getActivePlayers();
    void resetTable();
    void moveDealer();
    void setCurrentPlayer(final int index);
    PlayerModel getIthPlayerFromDealer(final int index);
}
