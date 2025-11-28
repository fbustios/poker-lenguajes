package org.poker.model;

public record Card(int value, String suit) {

    @Override
    public String toString() {
        String valueStr = switch (value) {
            case 11 -> "J";
            case 12 -> "Q";
            case 13 -> "K";
            case 14 -> "A";
            default -> String.valueOf(value);
        };
        return valueStr + " of " + suit;
    }
}
