package poker.dealing;

import poker.items.Card;
import poker.items.Deck;
import poker.items.Player;
import poker.table.PokerTable;

import java.util.ArrayList;
import java.util.List;

public final class HoldemDealingMethod implements DealingMethod {
    private enum Stage {PRE_FLOP, FLOP, TURN, RIVER};
    private Stage actualStage;
    private final List<Card> communityCards;

    public HoldemDealingMethod() {
        actualStage = Stage.PRE_FLOP;
        this.communityCards = new ArrayList<>();

    }

    @Override
    public void deal(PokerTable table, Deck deck) {
        switch (actualStage) {
            case PRE_FLOP -> {
                deck.refill();
                deck.shuffle();
                preFlop(table, deck);
                actualStage = Stage.FLOP;
            }
            case FLOP -> {
                flop(deck);
                actualStage = Stage.TURN;
            }
            case TURN -> {
                turn(deck);
                actualStage = Stage.RIVER;
            }
            case RIVER -> river(deck);
        }
    }

    @Override
    public List<Card> getCommunityCards() {
        return this.communityCards;
    }


    private void preFlop(PokerTable table, Deck deck) {
        for (Player p : table.getActivePlayers()) {
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

