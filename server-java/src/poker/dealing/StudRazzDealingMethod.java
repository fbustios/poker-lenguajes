package poker.dealing;

import poker.items.Card;
import poker.items.Deck;
import poker.rounds.Street;
import poker.table.PokerTable;
import poker.items.Player;

import java.util.List;


public class StudRazzDealingMethod implements DealingMethod {
    private Street currentStage = Street.THIRD;

    @Override
    public void deal(PokerTable table, Deck deck) {
        switch (currentStage){
            case THIRD -> {
                dealThirdStreet(table, deck);
                dealOneUpCard(table, deck);
                currentStage = Street.FOURTH;
            }
            case FOURTH -> {
                dealOneUpCard(table, deck);
                currentStage = Street.FIFTH;
            }
            case FIFTH -> {
                dealOneUpCard(table, deck);
                currentStage = Street.SIXTH;
            }

            case SIXTH -> {
                dealOneUpCard(table, deck);
                currentStage = Street.SEVENTH;
            }

            case SEVENTH -> {
                dealSeventhStreet(table, deck);
            }


        }

    }

    @Override
    public List<Card> getCommunityCards() {
        return List.of();
    }

    private void dealThirdStreet(PokerTable table, Deck deck){
        List<Player> players = table.getActivePlayers();
        for (Player player : players) {
            player.receiveCard(deck.draw());
            player.receiveCard(deck.draw());
        }
    }

    private void dealOneUpCard(PokerTable table, Deck deck) {
        List<Player> players = table.getActivePlayers();
        for (Player player : players) {
            if (!player.isFolded()) {
                player.receiveCard(deck.draw());
            }
        }
    }

    private void dealSeventhStreet(PokerTable table, Deck deck) {
        List<Player> players = table.getActivePlayers();
        for (Player player : players) {
            player.receiveCard(deck.draw());
        }
    }
}
