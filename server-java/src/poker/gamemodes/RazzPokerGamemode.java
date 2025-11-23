package poker.gamemodes;

import poker.items.Deck;
import poker.items.PlayerModel;
import poker.table.PokerTable;
import poker.dealing.Dealer;
import poker.table.PokerTable;

import java.util.Optional;


public class RazzPokerGamemode implements PokerGamemode{
    private Dealer dealer;
    private PokerTable table;
    private Deck deck;

    @Override
    public boolean isOver() {
        return false;
    }

    @Override
    public void play(PokerAction lastPokerAction) {

    }

    @Override
    public Optional<PlayerModel> getNextTurn() {
        return Optional.empty();
    }

    @Override
    public void distributePot() {

    }
}
