package poker.items;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ListDeck implements Deck{
    private List<Card> fullCards;
    private List<Card> cards;

    private ListDeck(List<Card> fullCards) {
        this.fullCards = fullCards;
        this.cards = fullCards;
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

    public static Deck build() {
        final List<Card> cards = new ArrayList<>();
        final Rank[] ranks = Rank.values();
        final Suit[] suits = Suit.values();
        for (Suit suit : suits) {
            for (Rank rank : ranks) {
                Card card = new Card(suit, rank, false);
                cards.add(card);
            }
        }
        return new ListDeck(cards);
    }
}
