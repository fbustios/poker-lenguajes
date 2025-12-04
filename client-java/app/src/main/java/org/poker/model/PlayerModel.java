package org.poker.model;

import java.util.ArrayList;
import java.util.List;

public final class PlayerModel {
    private String name;
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

                String suit = CardParser.parseSuit(suitChar);
                String value = CardParser.parseValue(valueStr);

                this.cards.add(new Card(suit, value, false));
            }
        }
    }

    public void setMoney(int money) {
        this.money = money;
    }
}