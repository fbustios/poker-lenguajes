package poker.gamemodes;

import poker.dealing.DealingMethod;
import poker.items.Deck;
import poker.items.Player;
import poker.pot.Pot;
import poker.pot.PotDistributer;
import poker.rounds.HoldemRound;
import poker.rounds.TurnManager;

import java.util.Optional;

public final class HoldemPokerGamemode implements PokerGamemode {
    private final Gamemode name = Gamemode.HOLD_EM;
    private final DealingMethod dealingMethod;
    private HoldemRound currentRound;
    private final TurnManager turnManager;
    private final PotDistributer potDistributer;
    private final Pot pot;
    private Deck deck;

    public HoldemPokerGamemode(final DealingMethod dealingMethod,
                               final TurnManager turnManager,
                               final PotDistributer pt,
                               final Pot pot,
                               final Deck deck) {
        this.pot = pot;
        currentRound = HoldemRound.PRE_FLOP;
        this.turnManager = turnManager;
        this.potDistributer = pt;
        this.dealingMethod = dealingMethod;
        this.deck = deck;
    }


    @Override
    public void play(PokerAction lastPokerAction) {
        if (!checkAction()) {
            throw new IllegalStateException();
        }
        
        PlayerAction action = lastPokerAction.action();
        switch (action) {  //aceptable porque es inmutable estos cambios o por lo menos nunca los voy a tocar
            case RAISE -> handleRaise(lastPokerAction.player(),lastPokerAction.bet());
            case CALL -> handleCall(lastPokerAction.player(), lastPokerAction.bet());
            case CHECK -> handleCheck(lastPokerAction.player());
            case ALL_IN -> handleAllIn(lastPokerAction.player(), lastPokerAction.bet());
            case FOLD -> handleFold(lastPokerAction.player());
        }
    }

    @Override
    public boolean isOver() {
        return turnManager.isRoundOver();
    }

    @Override
    public Optional<Player> getNextTurn() {
        return turnManager.nextTurn();
    }

    @Override
    public Gamemode getName() {
        return name;
    }

    @Override
    public void distributePot() {
        potDistributer.distribute();
    }

    private void setUpGame() {
        deck.refill();
    }

    private boolean checkAction() {
        return true;
    }

    private void handleRaise(Player player, int bet) {
        player.addMoney(-bet);
        turnManager.resetTurnsLeft();
        turnManager.setPendingAction(false);
    }

    private void handleAllIn(Player player, int bet) {
        int lastBet = 0;
        if (bet > lastBet) {
            turnManager.resetTurnsLeft();
        }
        player.addMoney(-player.getMoney());
        player.setAllIn(true);
        turnManager.setPendingAction(false);
    }

    private void handleFold(Player player) {
        player.setFolded(true);
        turnManager.setPendingAction(false);
    }


    private void handleCheck(Player player) {
        turnManager.setPendingAction(false);
    }

    private void handleCall(Player player, int bet) {
        player.addMoney(-bet);
        turnManager.setPendingAction(false);
    }


}
