package org.poker.model;

public record Card(String suit, String value, boolean hidden) {
    @Override
    public String toString() {
        if (hidden) {
            return "?";
        }
        return value + " of " + suit;
    }

    public String toCompactString() {
        if (hidden) return "?";

        char suitChar = switch (suit) {
            case "Hearts" -> 'H';
            case "Diamonds" -> 'D';
            case "Clubs" -> 'C';
            case "Spades" -> 'S';
            default -> '?';
        };

        String valueStr = switch (value) {
            case "10" -> "T";
            case "Jack" -> "J";
            case "Queen" -> "Q";
            case "King" -> "K";
            case "Ace" -> "A";
            default -> value;
        };

        return String.valueOf(suitChar) + valueStr;
    }
}
