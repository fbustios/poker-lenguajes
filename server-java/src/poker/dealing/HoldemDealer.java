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
                preFlop();
                actualStage = Stage.FLOP;
            }
            case FLOP -> {
                flop();
            }
            case TURN -> turn();
            case RIVER -> river();
        }
    }


    private void preFlop() {

    }

    private void flop() {

    }
    private void turn() {

    }

    private void river() {

    }
}
