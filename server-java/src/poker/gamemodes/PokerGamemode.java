package poker.gamemodes;

import poker.items.PlayerModel;

import java.util.Optional;

public interface PokerGamemode {
    boolean isOver();
    void play(Action lastAction);
    Optional<PlayerModel> getNextTurn();
}
