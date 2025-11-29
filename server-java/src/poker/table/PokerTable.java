package poker.table;

import poker.items.Player;

import java.util.List;

public interface PokerTable {
    Player next();
    List<Player> getPlayers();
    List<Player> getActivePlayers();
    void resetTable();
    void moveDealer();
    void setCurrentPlayer(Player player);
    Player getIthPlayerFromDealer(final int index);
}
