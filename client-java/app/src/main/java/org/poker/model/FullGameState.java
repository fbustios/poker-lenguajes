package org.poker.model;

import java.util.ArrayList;
import java.util.List;

public class FullGameState {
    private String gameMode;
    private String currentRound;
    private int pot;
    private String dealer;
    private String nextPlayer;
    private String smallBlind;
    private String bigBlind;
    private List<PlayerInfo> players;
    private List<Card> communityCards;

    public FullGameState() {
        this.players = new ArrayList<>();
        this.communityCards = new ArrayList<>();
    }
}
