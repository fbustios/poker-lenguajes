package pokertable;

import pokeritems.PlayerModel;

import java.util.List;

public final class CircularLinkedListPokerTable implements PokerTable{
    private Seat dealer;
    private Seat current;

    private CircularLinkedListPokerTable() {

    }

    @Override
    public PlayerModel next() {
      return null;
    }




}
