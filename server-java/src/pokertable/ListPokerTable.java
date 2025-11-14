package pokertable;

import pokeritems.PlayerModel;

import java.util.List;

public final class ListPokerTable implements PokerTable{
    private List<PlayerModel> playerModels;

    private ListPokerTable() {

    }

    @Override
    public PlayerModel next() {
      return null;
    }

}
