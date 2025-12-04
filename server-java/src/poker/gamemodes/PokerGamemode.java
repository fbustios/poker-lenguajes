package poker.gamemodes;

import poker.GameState;
import poker.items.Player;

import java.util.Optional;

public interface PokerGamemode {
    boolean isOver();
    void play(PokerAction lastPokerAction);
    Optional<Player> getNextTurn();
    Gamemode getName();
    void distributePot();
    String getDetails();
    boolean isRoundOver();
    void deal();
}
