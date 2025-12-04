package poker.handranking;

import poker.items.Player;

import java.util.List;

public interface RankingSystem {
    List<Player> rank(List<Player> players, char gamemode);
}
