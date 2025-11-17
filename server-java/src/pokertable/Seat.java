package pokertable;

import pokeritems.PlayerModel;

public final class Seat {
    private final boolean isDealer;
    private final int seatNumber;
    private boolean hasFolded;
    private Seat next;
    private final PlayerModel player;

    public Seat(final int seatNumber, final PlayerModel player) {
        this.seatNumber = seatNumber;
        this.player = player;
        this.isDealer = false;
    }

    public void setNext(final Seat next) {
        this.next = next;
    }

    public Seat getNext() {
        return this.next;
    }

    public boolean hasFolded() {
        return this.hasFolded;
    }

    public void setHasFolded(final boolean value) {
        this.hasFolded = value;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public PlayerModel getPlayer() {
        return player;
    }
}
