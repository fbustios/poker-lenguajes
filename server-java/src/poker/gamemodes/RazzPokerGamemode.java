package poker.gamemodes;

import poker.dealing.DealingMethod;
import poker.items.Deck;
import poker.items.Player;
import poker.rounds.TurnManager;
import poker.table.PokerTable;

import java.util.Optional;


public final class RazzPokerGamemode implements PokerGamemode{
    private TurnManager turnManager;
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
    public Gamemode getName() {
        return null;
    }

    @Override
    public void distributePot() {

    }

    @Override
    public String getDetails() {
        return turnManager.getDetails();
    }

    @Override
    public boolean isRoundOver() {
        return false;
    }

    @Override
    public void deal() {

    }

    @Override
    public void setStartingPlayer() {
        turnManager.setStartingPlayer();
    }
}
