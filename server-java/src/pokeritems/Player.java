package pokeritems;

import java.util.List;

public final class Player {
    private final int name;
    private List<Card> cards;

    public Player(int name, List<Card> cardList) {
        this.name = name;
        this.cards = cardList;
    }

    public void receiveCard(Card card) {
        this.cards.add(card);
    }

    public List<Card> getCards() {
        return this.cards;
    }
}
