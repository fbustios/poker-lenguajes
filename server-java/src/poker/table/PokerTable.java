package poker.table;

import poker.items.Player;

import java.util.List;

public interface PokerTable {
    List<Player> getPlayers();
    List<Player> getActivePlayers();
    void resetTable();
    int nextActivePlayerIndex(int i);
    void setCurrentPlayer(int i);
    Player next();
}
