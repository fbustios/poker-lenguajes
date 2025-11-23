package poker.gamemodes;

import poker.items.Deck;
import poker.items.Player;
import poker.table.PokerTable;
import poker.dealing.Dealer;

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
    public Optional<Player> getNextTurn() {
        return Optional.empty();
    }

    @Override
    public void distributePot() {

    }
}
