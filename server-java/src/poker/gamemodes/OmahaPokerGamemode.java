package poker.gamemodes;

import poker.dealing.DealingMethod;
import poker.items.Deck;
import poker.items.Player;
import poker.pot.Pot;
import poker.pot.PotDistributer;
import poker.rounds.HoldemRound;
import poker.rounds.TurnManager;

import java.util.Optional;

public class OmahaPokerGamemode implements PokerGamemode {
    private final Gamemode name = Gamemode.HOLD_EM;
    private final DealingMethod dealingMethod;
    private HoldemRound currentRound;
    private final TurnManager turnManager;
    private final PotDistributer potDistributer;
    private final Pot pot;
    private Deck deck;

    public OmahaPokerGamemode(DealingMethod dealingMethod, TurnManager turnManager, PotDistributer potDistributer, Pot pot) {
        this.dealingMethod = dealingMethod;
        this.turnManager = turnManager;
        this.potDistributer = potDistributer;
        this.pot = pot;
    }

    @Override
    public boolean isOver() {
        return false;
    }

    @Override
    public void play(PokerAction lastPokerAction) {

    }

    @Override
    public Optional<Player> getNextTurn() {
        return Optional.empty();
    }

    @Override
    public Gamemode getName() {
        return null;
    }

    @Override
    public void distributePot() {

    }

    @Override
    public String getDetails() {
        return turnManager.getDetails();
    }
}
