package poker;

import poker.gamemodes.Gamemode;
import poker.items.Player;

import java.util.List;

public interface GameState {
    List<Player> getPlayers();
    int getPot();
    Gamemode getCurrentGamemode();
}
