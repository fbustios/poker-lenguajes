package org.poker.model;

public record Card(String suit, String value, boolean hidden) {
    @Override
    public String toString() {
        if (hidden) {
            return "?";
        }
        return value + " of " + suit;
    }
    public String getImageKey() {
        if (hidden) {
            return null;
        }

        String normalizedSuit = suit.toUpperCase();
        int imageIndex = switch (value) {
            case "Ace" -> 1;
            case "Jack" -> 11;
            case "Queen" -> 12;
            case "King" -> 13;
            default -> {
                try {
                    yield Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    yield 0; // Valor inválido
                }
            }
        };

        return normalizedSuit + "_" + imageIndex;
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