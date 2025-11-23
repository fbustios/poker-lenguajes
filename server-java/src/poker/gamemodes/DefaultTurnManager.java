package poker.gamemodes;

import poker.items.Player;

import java.util.Optional;

public class DefaultTurnManager implements TurnManager{
    @Override
    public boolean isRoundOver() {
        return false;
    }

    @Override
    public Optional<Player> nextTurn() {
        return Optional.empty();
    }

    @Override
    public void setStartingPlayer() {

    }
}
