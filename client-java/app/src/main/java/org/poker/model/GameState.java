package org.poker.model;

import java.util.ArrayList;
import java.util.List;

public final class GameState {
    private String gameMode;
    private String gameModeRound;
    private String nextPlayer;
    private String dealer;
    private String smallBlind;
    private String bigBlind;
    private int pot;
    private int players_left;
    private List<PlayerModel> players;
    private List<Card> communityCards;
    private int last_raise;

    public GameState() {
        this.players = new ArrayList<>();
        this.communityCards = new ArrayList<>();
        this.pot = 0;
    }

    public List<Card> getCommunityCards() {
        return communityCards;
    }

    public void setLast_raise(int last_raise) {
        this.last_raise = last_raise;
    }

    public void setPlayers_left(int players_left) {
        this.players_left = players_left;
    }

    public void setGameMode(String mode) {
        this.gameMode = mode;
    }

    public void setGameModeRound(String roundMode) {
        this.gameModeRound = roundMode;
    }

    public void setNextPlayer(String player) {
        this.nextPlayer = player;
    }

    public void setDealer(String player) {
        this.dealer = player;
    }

    public void setSmallBlind(String small) {
        this.smallBlind = small;
    }

    public void setBigBlind(String big) {
        this.bigBlind = big;
    }

    public void setPot(int money) {
        this.pot = money;
    }

    public String getGameModeRound() {
        return gameModeRound;
    }

    public String getNextPlayer() {
        return nextPlayer;
    }

    public String getDealer() {
        return dealer;
    }

    public int getPot() {
        return pot;
    }

    public List<PlayerModel> getPlayers() {
        return players;
    }

    public void playersClear() {
        this.players.clear();
    }

    public void playersAdd(PlayerModel player) {
        this.players.add(player);
    }

    public void setCommunityCardsFromString(String cardsString) {
        communityCards = CardParser.communityCardsFromString(cardsString);
    }
}
