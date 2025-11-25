package poker.gamemodes;

import poker.items.Player;
import poker.table.PokerTable;

import java.util.Optional;

public class DefaultTurnManager implements TurnManager{
    private boolean pendingAction;
    private final PokerTable table;

    public DefaultTurnManager(PokerTable table) {
        this.table = table;
        this.pendingAction = false;
    }

    @Override
    public boolean isRoundOver() {

        return false;
    }

    @Override
    public Optional<Player> nextTurn() {
        if (this.pendingAction) {
            return Optional.empty();
        }
        this.pendingAction = true;
        return Optional.empty();
    }

    @Override
    public void setStartingPlayer() {
        table.setCurrentPlayer();
    }

    public void setPendingAction(boolean value) {
        this.pendingAction = value;
    }
}
