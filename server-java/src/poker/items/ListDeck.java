package poker.items;
import java.util.Collections;
import java.util.List;


public class ListDeck implements Deck{
    private final List<Card> fullCards;
    private List<Card> cards;

    public ListDeck(List<Card> fullCards) {
        this.fullCards = fullCards;
    }

    @Override
    public void shuffle() {
        Collections.shuffle(cards);
    }

    @Override
    public Card draw() {
        return cards.removeFirst();
    }

    @Override
    public void refill() {
        this.cards = List.copyOf(fullCards);
        shuffle();
    }
}
