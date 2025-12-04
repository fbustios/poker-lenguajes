package poker.dealing;

import poker.items.Deck;
import poker.table.PokerTable;
import poker.items.Card;
import poker.items.Player;

import java.util.ArrayList;
import java.util.List;

public class OmahaDealingMethod implements DealingMethod {

    private enum Stage {PRE_FLOP, FLOP, TURN, RIVER}

    private Stage actualStage = Stage.PRE_FLOP;

    private final List<Card> communityCards = new ArrayList<>();



    @Override
    public void deal(PokerTable table, Deck deck) {
        List<Player> players = table.getPlayers();
        switch (actualStage) {
            case PRE_FLOP -> {
                preFlop(table, deck);
                actualStage = OmahaDealingMethod.Stage.FLOP;
            }
            case FLOP -> {
                flop(deck);
                actualStage = OmahaDealingMethod.Stage.TURN;
            }
            case TURN -> {
                turn(deck);
                actualStage = OmahaDealingMethod.Stage.RIVER;
            }
            case RIVER -> river(deck);
        }
    }

    @Override
    public List<Card> getCommunityCards() {
        return this.communityCards;
    }


    private void preFlop(PokerTable table, Deck deck) {
        deck.refill();
        deck.shuffle();
        for (Player p : table.getActivePlayers()) {
            p.receiveCard(deck.draw());
            p.receiveCard(deck.draw());
            p.receiveCard(deck.draw());
            p.receiveCard(deck.draw());
        }
    }

    private void flop(Deck deck) {
        for (int i = 0; i < 3; i++){
            Card c  = deck.draw();
            communityCards.add(c.faceUp());
        }
    }

    private void turn(Deck deck) {
        Card c  = deck.draw();
        communityCards.add(c.faceUp());
    }

    private void river(Deck deck) {
        Card c  = deck.draw();
        communityCards.add(c.faceUp());
    }
}
