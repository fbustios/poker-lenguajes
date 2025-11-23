package poker.gamemodes;

import poker.items.Player;

import java.util.Optional;

public interface PokerGamemode {
    boolean isOver();
    void play(PokerAction lastPokerAction);
    Optional<Player> getNextTurn();

    void distributePot();
}
