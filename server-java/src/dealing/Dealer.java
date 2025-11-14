package dealing;

import pokeritems.Deck;
import pokeritems.Player;

import java.util.List;

public interface Dealer {
    void deal(List<Player> players, Deck deck);
}
