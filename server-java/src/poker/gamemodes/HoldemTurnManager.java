package poker.gamemodes;

import poker.items.Player;
import poker.table.PokerTable;

import java.util.Optional;

public class HoldemTurnManager implements TurnManager{
    private boolean pendingAction;
    private final PokerTable table;
    private int turnsLeft;

    public HoldemTurnManager(PokerTable table) {
        this.table = table;
        this.pendingAction = false;
    }

    @Override
    public boolean isRoundOver() {
        return !pendingAction && turnsLeft <= 0;
    }

    @Override
    public Optional<Player> nextTurn() {
        if (this.pendingAction) {
            return Optional.empty();
        }
        if (isRoundOver()) {

        }
        this.turnsLeft--;
        this.pendingAction = true;
        return Optional.of(table.next());
    }

    @Override
    public void setStartingPlayer() {
        this.turnsLeft = table.getActivePlayers().size();
        Player current = table.getIthPlayerFromDealer(1);
        table.setCurrentPlayer(current);
    }

    @Override
    public void setPendingAction(boolean value) {
        this.pendingAction = value;
    }
}
