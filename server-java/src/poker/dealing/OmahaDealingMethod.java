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


    private void preFlop(PokerTable table, Deck deck) {
        int playerCount = table.getActivePlayers().size();
        for(int i = 0; i < playerCount; i++){
            Player player = table.next();
            if (player != null) {
                player.receiveCard(deck.draw());
                player.receiveCard(deck.draw());
                player.receiveCard(deck.draw());
                player.receiveCard(deck.draw());
            }
        }
    }

    private void flop(Deck deck) {
        for (int i = 0; i < 3; i++){
            communityCards.add(deck.draw());
        }
    }

    private void turn(Deck deck) {
        communityCards.add(deck.draw());
    }

    private void river(Deck deck) {
        communityCards.add(deck.draw());

    }
}
