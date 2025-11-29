package poker.pot;

import poker.items.Player;

public interface Pot {
    boolean isEmpty();
    int takeAll();
    void setSidePots(Player player, int amount);
    void add(int amount);
    int getAmount();
}
