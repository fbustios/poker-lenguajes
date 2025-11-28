package poker.dealing;

import poker.items.Deck;
import poker.table.PokerTable;


public interface DealingMethod {
    void deal(PokerTable table, Deck deck);
}
