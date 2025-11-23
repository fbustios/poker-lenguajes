package poker;

import poker.gamemodes.PokerAction;
import poker.items.Player;

import java.util.Optional;

public interface PokerGame {
    boolean isGameFinished();
    void startGame();
    boolean isGamemodeOver();
    Player getWinner();
    void play(PokerAction player);
    Optional<Player> nextTurn();

}

