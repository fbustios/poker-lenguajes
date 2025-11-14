package pokertable;

import pokeritems.PlayerModel;

public final class Seat {
    private final int seatNumber;
    private boolean hasFolded;
    private Seat next;
    private PlayerModel player;

    public Seat(final int seatNumber, PlayerModel player) {
        this.seatNumber = seatNumber;
        this.player = player;
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

    public void setHasFolded(boolean value) {
        this.hasFolded = value;
    }
}
