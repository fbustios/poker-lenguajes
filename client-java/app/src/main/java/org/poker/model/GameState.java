package org.poker.model;

import java.util.ArrayList;
import java.util.List;

public final class GameState {
    public String gameMode;
    public String gameModeRound;
    public String nextPlayer;
    public String dealer;
    public String smallBlind;
    public String bigBlind;
    public int pot;
    public List<PlayerModel> players;

    public GameState() {
        this.players = new ArrayList<>();
        this.pot = 0;
    }
}
