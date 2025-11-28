package poker.gamemodes;

import poker.dealing.DealingMethod;
import poker.items.Deck;
import poker.items.Player;
import poker.table.PokerTable;

import java.util.Optional;


public class RazzPokerGamemode implements PokerGamemode{
    private DealingMethod dealingMethod;
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
    public Optional<Player> getNextTurn() {
        return Optional.empty();
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public void distributePot() {

    }
}
