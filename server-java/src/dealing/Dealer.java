package dealing;

import pokeritems.Deck;
import pokeritems.PlayerModel;
import pokertable.PokerTable;

import java.util.List;

public interface Dealer {
    void deal(PokerTable table, Deck deck);
}
