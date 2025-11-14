package dealing;

import pokeritems.Deck;
import pokeritems.PlayerModel;

import java.util.List;

public final class HoldemDealer implements Dealer {
    private enum Stage {PRE_FLOP, FLOP, TURN, RIVER};
    Stage actualStage;

    public HoldemDealer() {
        actualStage = Stage.PRE_FLOP;
    }

    @Override
    public void deal(List<PlayerModel> playerModels, Deck deck) {
        switch (actualStage) {
            case PRE_FLOP -> preFlop();
            case FLOP -> flop();
            case TURN -> turn();
            case RIVER -> river();
        }
        nextStage();
    }

    private void nextStage() {
        switch (actualStage) {
            case PRE_FLOP -> this.actualStage = Stage.FLOP;
            case FLOP -> this.actualStage = Stage.TURN;
            case TURN -> this.actualStage = Stage.RIVER;
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
