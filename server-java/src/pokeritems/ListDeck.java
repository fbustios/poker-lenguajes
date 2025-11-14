package pokeritems;
import java.util.Collections;
import java.util.List;


public class ListDeck implements Deck{
    private List<Card> cards;

    @Override
    public void shuffle() {
        Collections.shuffle(cards);
    }

    @Override
    public Card draw() {
        return cards.removeFirst();
    }
}
