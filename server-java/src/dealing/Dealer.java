package dealing;

import pokeritems.Deck;
import pokeritems.PlayerModel;

import java.util.List;

public interface Dealer {
    void deal(List<PlayerModel> playerModels, Deck deck);
}
