package poker.pot;

import poker.items.PlayerModel;

import java.util.List;

public interface PotDistributer {
    List<PlayerModel> distribute();
}
