package poker;

import poker.gamemodes.Action;
import poker.items.PlayerModel;

import java.util.List;
import java.util.Optional;

public interface PokerGame {
    boolean isGameFinished();
    void startGame();
    boolean isGamemodeOver();
    Optional<List<PlayerModel>> getWinners();
    Optional<PlayerModel> play(Action player);
}
