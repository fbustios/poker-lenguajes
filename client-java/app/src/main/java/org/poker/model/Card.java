package org.poker.model;

public record Card(int value, String suit) {
    private static final String QUEEN_LETTER = "Q";
    private static final String KING_LETTER = "K";
    private static final String ACE_LETTER = "A";
    private static final String JACK_LETTER = "J";

    private static final int JACK_VALUE = 11;
    private static final int QUEEN_VALUE = 12;
    private static final int KING_VALUE = 13;
    private static final int ACE_VALUE = 14;

    public String getImageKey() {
        String normalizedSuit = suit.toUpperCase();
        int imageIndex = value;
        if (value == ACE_VALUE) {
            imageIndex = 1;
        }

        return normalizedSuit + "_" + imageIndex;
    }

    @Override
    public String toString() {
        final String valueStr = switch (value) {
            case JACK_VALUE -> JACK_LETTER;
            case QUEEN_VALUE -> QUEEN_LETTER;
            case KING_VALUE -> KING_LETTER;
            case ACE_VALUE -> ACE_LETTER;
            default -> String.valueOf(value);
        };
        return valueStr + " of " + suit;
    }
}
