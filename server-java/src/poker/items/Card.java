package poker.items;

public record Card(Suit suit, Rank rank, boolean seen) {
    @Override
    public String toString() {
        return suit.getRep() + rank.toString();
    }
}
