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




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getMoney() {
        return money;
    }


    public List<Card> getCards() {
        return cards;
    }

    public String getCardsString() {
        if (cards.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cards.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(cards.get(i).toString());
        }
        return sb.toString();
    }

    private String parseSuit(char suitChar) {
        return switch (suitChar) {
            case 'H' -> "Hearts";
            case 'D' -> "Diamonds";
            case 'C' -> "Clubs";
            case 'S' -> "Spades";
            default -> "Unknown";
        };
    }

    private String parseValue(String valueStr) {
        return switch (valueStr) {
            case "T" -> "10";
            case "J" -> "Jack";
            case "Q" -> "Queen";
            case "K" -> "King";
            case "A" -> "Ace";
            default -> valueStr;
        };
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}