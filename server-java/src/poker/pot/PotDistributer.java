package poker.pot;

import poker.items.Player;

import java.util.List;

public interface PotDistributer {
    List<Player> distribute(char gamemode);
}
