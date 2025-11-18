package poker.table;

import poker.items.PlayerModel;

import java.util.List;

public final class CircularLinkedListPokerTable implements PokerTable{
    private Seat dealer;
    private Seat current;

    public CircularLinkedListPokerTable(Seat dealer) {
        this.dealer = dealer;
        this.current = dealer;
    }

    @Override
    public PlayerModel next() {
      Seat next = this.current.getNext();
      this.current = next;
      return next.getPlayer();
    }


    @Override
    public List<PlayerModel> getPlayers() {
        return List.of();
    }

    @Override
    public void resetTable() {
        Seat curr = current;
        while(current != dealer) {
            curr.setHasFolded(false);
            curr = curr.getNext();
        }
    }
}
