package poker.gamemodes;

import poker.dealing.Dealer;
import poker.items.Deck;
import poker.items.Player;
import poker.pot.PotDistributer;
import java.util.Optional;

public final class HoldemPokerGamemode implements PokerGamemode {
    private enum Stage {PRE_FLOP, FLOP, TURN, RIVER};
    private final Dealer dealer;
    private HoldemRound currentRound;
    private final TurnManager turnManager;
    private final PotDistributer potDistributer;
    private Deck deck;
    private final boolean gameStarted;

    public HoldemPokerGamemode(Dealer dealer, TurnManager turnManager, PotDistributer pt, Deck deck) {
        this.turnManager = turnManager;
        this.potDistributer = pt;
        this.gameStarted = false;
        this.dealer = dealer;
        this.deck = deck;
    }


    @Override
    public void play(PokerAction lastPokerAction) {
        if (!checkAction()) {
            throw new IllegalStateException();
        }
        if(!gameStarted) {}//raro corregir
        
        PlayerAction action = lastPokerAction.action();
        switch (action) {  //aceptable porque es inmutable estos cambios o por lo menos nunca los voy a tocar
            case RAISE -> handleRaise(lastPokerAction.player(),lastPokerAction.bet());
            case CALL -> handleCall(lastPokerAction.player());
            case CHECK -> handleCheck(lastPokerAction.player());
            case ALL_IN -> handleAllIn(lastPokerAction.player(), lastPokerAction.bet());
            case FOLD -> handleFold(lastPokerAction.player());
        }
    }

    @Override
    public boolean isOver() {
        return false;
    }

    @Override
    public Optional<Player> getNextTurn() {
        if(turnManager.isRoundOver()) {
            nextRound();
            //dealer.deal(table, deck);
        }
        return turnManager.nextTurn();
    }

    @Override
    public void distributePot() {
        potDistributer.distribute();
    }

    private void nextRound() {
        turnManager.setStartingPlayer();
    }


    private void setUpGame() {
        deck.refill();

    }

    private boolean checkAction() {
        return true;
    }

    private void handleRaise(Player player, int bet) {
    }

    private void handleAllIn(Player player, int bet) {
    }

    private void handleFold(Player player) {
    }


    private void handleCheck(Player player) {
    }

    private void handleCall(Player player) {
    }


}
