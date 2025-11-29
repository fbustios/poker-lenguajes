package poker.table;

import poker.items.Player;

import java.util.ArrayList;
import java.util.List;

public final class CircularLinkedListPokerTable implements PokerTable{
    private Seat dealer;
    private Seat current;

    public CircularLinkedListPokerTable(Seat dealer) {
        this.dealer = dealer;
        this.current = dealer;
    }

    @Override
    public Player next() {
      Seat next = this.current.getNext();
      this.current = next;
      return next.getPlayer();
    }


    @Override
    public List<Player> getPlayers() {
        final List<Player> activePlayers = new ArrayList<>();
        Seat currentSeat = current;
        final int startingSeat = currentSeat.getSeatNumber();
        do {
            final Player currentPlayer = currentSeat.getPlayer();
            activePlayers.add(currentPlayer);
            currentSeat = currentSeat.getNext();
        } while (currentSeat.getSeatNumber() != startingSeat);

        return activePlayers;
    }

    @Override
    public List<Player> getActivePlayers() {
        final List<Player> activePlayers = new ArrayList<>();
        Seat currentSeat = current;
        final int startingSeat = currentSeat.getSeatNumber();
        do {
            final Player currentPlayer = currentSeat.getPlayer();
            if(currentPlayer.isActive()) {
                activePlayers.add(currentPlayer);
            }
            currentSeat = currentSeat.getNext();
        } while (currentSeat.getSeatNumber() != startingSeat);

        return activePlayers;
    }

    @Override
    public void resetTable() {
        Seat currentSeat = current;
        final int startingSeat = currentSeat.getSeatNumber();
        do {
            final Player currentPlayer = currentSeat.getPlayer();
            if(currentPlayer.isActive()) {
                currentPlayer.setAllIn(false);
                currentPlayer.setFolded(false);
            }
            currentSeat = currentSeat.getNext();
        } while (currentSeat.getSeatNumber() != startingSeat);
    }

    @Override
    public void moveDealer() {
        this.dealer = dealer.getNext();
    }

    @Override
    public void setCurrentPlayer(Player player) {

    }

    @Override
    public Player getIthPlayerFromDealer(int i) {
        return null;
    }
}
