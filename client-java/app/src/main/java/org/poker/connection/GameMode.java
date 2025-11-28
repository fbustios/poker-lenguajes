package org.poker.connection;

public enum GameMode {
    HOLDEM("Holdem"),
    OMAHA("Omaha"),
    RAZZ("Razz"),
    STUD("Stud"),
    EIGHT("Eight");

    private final String name;

    GameMode(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
