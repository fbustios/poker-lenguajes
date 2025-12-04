package poker.items;

import java.util.List;

public final class Player {
    private final String name;
    private List<Card> cards;
    private int money;
    private boolean isActive;
    private boolean isFolded;
    private boolean isAllIn;
    private boolean isConnected;

    public Player(final String name, List<Card> cardList, final int initialBet) {
        this.name = name;
        this.cards = cardList;
        this.isActive = true;
        this.isAllIn = false;
        this.money = initialBet;
        this.isFolded = false;
        this.isConnected = true;
    }

    public void setConnected(boolean value) {
        this.setConnected(value);
    }

    public boolean isConnected() {
        return isConnected;
    }

    //añadí addMoney y removeMoney para manejar el dinero del
    //jugador cuando se reparte el pot
    public void addMoney(final int amount) { this.money += amount;}

    public void removeMoney(final int amount) { this.money -= amount;}

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
        isActive = false;
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
        isActive = false;
        isAllIn = allIn;
    }

    public void setCards(final List<Card> cards) {
        this.cards = cards;
    }
}
