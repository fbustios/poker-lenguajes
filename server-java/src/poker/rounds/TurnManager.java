package poker.rounds;

import poker.items.Player;
import poker.table.PokerTable;

import java.util.Optional;

public interface TurnManager {
    boolean isRoundOver();
    boolean isGameOver();
    Optional<Player> nextTurn();

    void setStartingPlayer();

    void setPendingAction(boolean value);
    void resetTurnsLeft();
    String getDetails();
    PokerTable getTable();
}
