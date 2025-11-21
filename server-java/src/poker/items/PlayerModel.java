package poker.items;

import java.util.List;

public final class PlayerModel {
    private final String name;
    private List<Card> cards;
    private int money;
    private boolean isActive;
    private boolean isFolded;
    private boolean isAllIn;

    public PlayerModel(final String name, final List<Card> cardList, final int initialBet) {
        this.name = name;
        this.cards = cardList;
        this.isActive = true;
        this.isAllIn = false;
        this.money = initialBet;
        this.isFolded = false;
    }

    public void receiveCard(final Card card) {
        this.cards.add(card);
    }

    public List<Card> getCards() {
        return this.cards;
    }

    public void setMoney(final int money) {
        this.money = money;
    }

    public int getMoney() {
        return money;
    }

    public boolean isFolded() {
        return isFolded;
    }

    public void setFolded(boolean folded) {
        isFolded = folded;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isAllIn() {
        return isAllIn;
    }

    public void setActive(final boolean active) {
        isActive = active;
    }

    public void setAllIn(final boolean allIn) {
        isAllIn = allIn;
    }

    public void setCards(final List<Card> cards) {
        this.cards = cards;
    }
}
