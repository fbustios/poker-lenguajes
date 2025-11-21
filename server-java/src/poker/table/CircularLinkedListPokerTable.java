package poker.table;

import poker.items.PlayerModel;

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
    public PlayerModel next() {
      Seat next = this.current.getNext();
      this.current = next;
      return next.getPlayer();
    }


    @Override
    public List<PlayerModel> getPlayers() {
        final List<PlayerModel> activePlayers = new ArrayList<>();
        Seat currentSeat = current;
        final int startingSeat = currentSeat.getSeatNumber();
        do {
            final PlayerModel currentPlayer = currentSeat.getPlayer();
            activePlayers.add(currentPlayer);
            currentSeat = currentSeat.getNext();
        } while (currentSeat.getSeatNumber() != startingSeat);

        return activePlayers;
    }

    @Override
    public List<PlayerModel> getActivePlayers() {
        final List<PlayerModel> activePlayers = new ArrayList<>();
        Seat currentSeat = current;
        final int startingSeat = currentSeat.getSeatNumber();
        do {
            final PlayerModel currentPlayer = currentSeat.getPlayer();
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
            final PlayerModel currentPlayer = currentSeat.getPlayer();
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
    public void setCurrentPlayer(int index) {

    }

    @Override
    public PlayerModel getIthPlayerFromDealer(int i) {
        return null;
    }
}
