package poker;

import poker.gamemodes.PokerAction;
import poker.items.Player;

import java.util.Optional;

public interface PokerGame { //aqui hay cosas que solapan responsabilidades con gamestate pero bueno
    boolean isGameFinished();
    void startGame();
    boolean isGamemodeOver();
    Player getWinner();
    void play(PokerAction player);
    Optional<Player> nextTurn();
    GameState getGameState();
    void setNextMode();

}

