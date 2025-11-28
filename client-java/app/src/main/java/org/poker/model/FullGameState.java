package org.poker.model;

import java.util.ArrayList;
import java.util.List;

public class FullGameState {
    public String gameMode;
    public String currentRound;
    public int pot;
    public String dealer;
    public String nextPlayer;
    public String smallBlind;
    public String bigBlind;
    public List<PlayerInfo> players;
    public List<Card> communityCards;

    public FullGameState() {
        this.players = new ArrayList<>();
        this.communityCards = new ArrayList<>();
    }
}
