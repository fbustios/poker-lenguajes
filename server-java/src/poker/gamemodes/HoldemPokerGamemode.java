package poker.gamemodes;

import poker.dealing.Dealer;
import poker.items.Deck;
import poker.items.PlayerModel;
import poker.pot.PotDistributer;
import java.util.Optional;

public final class HoldemPokerGamemode implements PokerGamemode {
    private enum Stage {PRE_FLOP, FLOP, TURN, RIVER};
    private final Dealer dealer;
    private HoldemRound currentRound;
    private final TurnManager turnManager;
    private PotDistributer pt;
    private Deck deck;
    private final boolean gameStarted;

    public HoldemPokerGamemode(Dealer dealer, TurnManager turnManager, Deck deck) {
        this.turnManager = turnManager;
        this.gameStarted = false;
        this.dealer = dealer;
        this.deck = deck;
    }


    @Override
    public void play(PokerAction lastPokerAction) {
        if (!checkAction()) {
            throw new IllegalStateException();
        }
        turnManager.updateGame();
    }


    @Override
    public boolean isOver() {
        return false;
    }

    @Override
    public Optional<PlayerModel> getNextTurn() {
        if(turnManager.isRoundOver()) {
            nextRound();
            dealer.deal(table, deck);
        }
        return turnManager.nextTurn();
    }


    private void start() {
        dealer.deal(table, deck);
    }

    private boolean checkAction() {

    }


}
