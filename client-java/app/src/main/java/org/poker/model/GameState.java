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
    public GameState() {
        this.players = new ArrayList<>();
        this.pot = 0;
    }

    public List<Card> getCommunityCards() {
        return communityCards;
    }

    public void setCommunityCards(List<Card> cards) {
        this.communityCards = cards;
    }

    public void addCommunityCard(Card card) {
        if (this.communityCards != null) {
            this.communityCards.add(card);
        }
    }
    public int getPlayers_left() {
        return players_left;
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

    public void setPlayers(List<PlayerModel> gamers) {
        this.players = gamers;
    }

    public String getGameMode() {
        return gameMode;
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

    public String getSmallBlind() {
        return smallBlind;
    }

    public String getBigBlind() {
        return bigBlind;
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
}
