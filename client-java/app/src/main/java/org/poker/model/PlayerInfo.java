package org.poker.model;

import java.util.ArrayList;
import java.util.List;

public class PlayerInfo {
    private String name;
    private int money;
    private List<Card> visibleCards;
    private int hiddenCardCount;
    private boolean isActive;
    private boolean isDealer;
    private boolean isSmallBlind;
    private boolean isBigBlind;

    public PlayerInfo(final String name) {
        this.name = name;
        this.visibleCards = new ArrayList<>();
        this.isActive = true;
    }
}
