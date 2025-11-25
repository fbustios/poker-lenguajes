package poker.items;

public interface Deck {
    void shuffle();
    Card draw();
    void refill();
}
