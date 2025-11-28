package org.poker.model;

import java.util.ArrayList;
import java.util.List;

public class PlayerInfo {
    public String name;
    public int money;
    public List<Card> visibleCards;
    public int hiddenCardCount;
    public boolean isActive;
    public boolean isDealer;
    public boolean isSmallBlind;
    public boolean isBigBlind;

    public PlayerInfo(String name) {
        this.name = name;
        this.visibleCards = new ArrayList<>();
        this.isActive = true;
    }
}
