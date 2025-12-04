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

    public void setMoney(int money) {
        this.money = money;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public void setCardsFromString(String cardsString) {
        this.cards.clear();

        if (cardsString == null || cardsString.isEmpty()) {
            return;
        }

        String[] cardTokens = cardsString.split(",");
        for (String token : cardTokens) {
            token = token.trim();
            if (token.isEmpty() || token.equals("?")) {
                this.cards.add(new Card(null, null, true));
            } else if (token.length() >= 2) {
                char suitChar = token.charAt(0);
                String valueStr = token.substring(1);

                String suit = parseSuit(suitChar);
                String value = parseValue(valueStr);

                this.cards.add(new Card(suit, value, false));
            }
        }
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