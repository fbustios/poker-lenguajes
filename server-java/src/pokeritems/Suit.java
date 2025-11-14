package pokeritems;

public enum Suit {
    HEARTS('H'), SPADES('S'), DIAMONDS('D'), CLOVERS('C');

    private final char rep;

    Suit(final char rep) {
        this.rep = rep;
    }

    public char getRep() {
        return rep;
    }
}
