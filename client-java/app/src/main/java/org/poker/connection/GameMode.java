package org.poker.connection;

public enum GameMode {
    HOLDEM("holdem"),
    OMAHA("omaha"),
    RAZZ("razz"),
    STUD("stud"),
    EIGHT("eight");

    private final String name;

    GameMode(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
