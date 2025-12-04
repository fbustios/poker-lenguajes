package org.poker.model;

import java.util.ArrayList;
import java.util.List;

public final class PlayerModel {
    private String name;
    private int index;
    private int money;
    private List<Card> cards;

    public PlayerModel() {
        this.cards = new ArrayList<>();
    }


    public List<Card> getCards() {
        return cards;
    }

    public int getMoney() {
        return money;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public void setName(String playerName) {
        this.name = playerName;
    }

    public void setIndex(int playerIndex) {
        this.index = playerIndex;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}