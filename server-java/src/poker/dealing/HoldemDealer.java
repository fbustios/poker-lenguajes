package poker.dealing;

import poker.items.Deck;
import poker.items.PlayerModel;
import poker.table.PokerTable;

import java.util.List;

public final class HoldemDealer implements Dealer {
    private enum Stage {PRE_FLOP, FLOP, TURN, RIVER};
    private Stage actualStage;

    public HoldemDealer() {
        actualStage = Stage.PRE_FLOP;
    }

    @Override
    public void deal(PokerTable table, Deck deck) {
        List<PlayerModel> players = table.getPlayers();
        switch (actualStage) {
            case PRE_FLOP -> {
                preFlop(table, deck);
                actualStage = Stage.FLOP;
            }
            case FLOP -> {
                flop();
                actualStage = Stage.TURN;
            }
            case TURN -> {
                turn();
                actualStage = Stage.RIVER;
            }
            case RIVER -> river();
        }
    }


    private void preFlop(PokerTable table, Deck deck) {
        int playerCount = table.getPlayers().size();
        for(int round = 0; round < 2; round ++){
            for(int i = 0; i < playerCount; i++){
                PlayerModel player = table.next();
                if (player != null) {
                    player.receiveCard(deck.draw());
                }
            }
        }


    }

    private void flop() {

    }
    private void turn() {

    }

    private void river() {

    }
}
