package poker.gamemodes;

import poker.items.Player;

import java.util.Optional;

public interface TurnManager {
    boolean isRoundOver();

    Optional<Player> nextTurn();

    void setStartingPlayer();

    void setPendingAction(boolean value);
}
