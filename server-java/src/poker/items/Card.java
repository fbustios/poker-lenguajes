package poker.items;

public record Card(Suit suit, Rank rank, boolean seen) {
    public Card faceUp() {
        return new Card(suit, rank, true);
    }
    public Card faceDown() {
        return new Card(suit, rank, false);
    }
    @Override
    public String toString() {
        return seen ? suit.getRep() + rank.toString() : "??";
    }
}
