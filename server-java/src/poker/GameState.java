package poker;

import poker.items.Player;

import java.util.List;

public interface GameState {
    List<Player> getPlayers();
    int getPot();
    String getCurrentGamemode();
}
