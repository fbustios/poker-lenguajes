package poker.gamemodes;

import poker.items.PlayerModel;

import java.util.Optional;

public interface TurnManager {
    boolean isRoundOver();

    Optional<PlayerModel> nextTurn();

    void setStartingPlayer();
}
