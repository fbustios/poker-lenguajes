package pot;

import pokeritems.PlayerModel;

import java.util.List;

public interface PotDistributer {
    List<PlayerModel> distribute(final List<PlayerModel> winners);
}
