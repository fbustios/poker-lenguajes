package poker.gamemodes;

import poker.items.PlayerModel;

import java.util.Optional;

public class DefaultTurnManager implements TurnManager{
    @Override
    public boolean isRoundOver() {
        return false;
    }

    @Override
    public Optional<PlayerModel> nextTurn() {
        return Optional.empty();
    }

    @Override
    public void setStartingPlayer() {

    }
}
