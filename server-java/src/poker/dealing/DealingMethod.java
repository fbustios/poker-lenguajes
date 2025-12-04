package poker.dealing;

import poker.items.Card;
import poker.items.Deck;
import poker.table.PokerTable;

import java.util.List;


public interface DealingMethod {
    void deal(PokerTable table, Deck deck);
    List<Card> getCommunityCards();
}
