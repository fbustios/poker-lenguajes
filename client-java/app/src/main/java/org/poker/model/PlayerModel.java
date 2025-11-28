package org.poker.model;

import java.util.ArrayList;
import java.util.List;

public final class PlayerModel {
    public String name;
    public int index;
    public int money;
    public List<Card> cards;

    public PlayerModel() {
        this.cards = new ArrayList<>();
    }
}
