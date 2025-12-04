package org.poker.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CardParser {
    public static String parseSuit(char suitChar) {
        return switch (suitChar) {
            case 'H' -> "Hearts";
            case 'D' -> "Diamonds";
            case 'C' -> "Clubs";
            case 'S' -> "Spades";
            default -> "Unknown";
        };
    }

    public static String parseValue(String valueStr) {
        return switch (valueStr) {
            case "T" -> "10";
            case "J" -> "Jack";
            case "Q" -> "Queen";
            case "K" -> "King";
            case "A" -> "Ace";
            default -> valueStr;
        };
    }

    public static List<Card> communityCardsFromString(String communityCards) {
        List<Card> communityCardList = new ArrayList<>();

        if (communityCards == null || communityCards.isEmpty()) {
            return Collections.emptyList();
        }

        String[] cardTokens = communityCards.split(",");
        for (String token : cardTokens) {
            token = token.trim();
            if (token.isEmpty() || token.equals("?")) {
                communityCardList.add(new Card(null, null, true));
            } else if (token.length() >= 2) {
                char suitChar = token.charAt(0);
                String valueStr = token.substring(1);

                String suit = parseSuit(suitChar);
                String value = parseValue(valueStr);

                communityCardList.add(new Card(suit, value, false));
            }
        }

        return communityCardList;
    }
}
