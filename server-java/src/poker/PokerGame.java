package poker;

import poker.gamemodes.PokerAction;
import poker.items.PlayerModel;

import java.util.List;
import java.util.Optional;

public interface PokerGame {
    boolean isGameFinished();
    void startGame();
    boolean isGamemodeOver();
    PlayerModel getWinner();
    Optional<PlayerModel> play(PokerAction player);
}
