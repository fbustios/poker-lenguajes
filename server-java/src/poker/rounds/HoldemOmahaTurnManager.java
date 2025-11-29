package poker.rounds;
import poker.items.Player;
import poker.table.PokerTable;
import java.util.List;
import java.util.Optional;

public class HoldemOmahaTurnManager implements TurnManager {
    private final List<HoldemRound> rounds;
    private int currentRound;
    private boolean pendingAction;
    private final PokerTable table;
    private int turnsLeft;
    private int dealer;

    public HoldemOmahaTurnManager(List<HoldemRound> rounds, PokerTable table) {
        this.rounds = rounds;
        this.currentRound = 0;
        this.dealer = 0;
        this.table = table;
        this.pendingAction = false;
    }

    @Override
    public boolean isRoundOver() {
        return !pendingAction && (turnsLeft <= 0);
    }

    @Override
    public Optional<Player> nextTurn() {
        if (this.pendingAction) {
            return Optional.empty();
        }

        if (isRoundOver()) {
            System.out.println("Round Ended");
            this.currentRound+=1;
            setStartingPlayer();
        }

        if (currentRound == rounds.size() - 1) {
            System.out.println("gamemode ended");
            return Optional.empty();
        }
        this.turnsLeft--;
        this.pendingAction = true;
        return Optional.of(table.next());
    }

    @Override
    public void setStartingPlayer() {
        if (currentRound == 0) {
            this.dealer = table.nextActivePlayerIndex(dealer);
            this.turnsLeft = table.getActivePlayers().size();
            int smallBlind = table.nextActivePlayerIndex(dealer);
            int bigBlind = table.nextActivePlayerIndex(smallBlind);
            table.setCurrentPlayer(bigBlind);
            return;
        }
        this.dealer = table.nextActivePlayerIndex(dealer);
        this.turnsLeft = table.getActivePlayers().size();
        //System.out.println(dealer);
        table.setCurrentPlayer(dealer);
    }

    @Override
    public void setPendingAction(boolean value) {
        this.pendingAction = value;
    }

    @Override
    public void resetTurnsLeft() {
        System.out.println("turns reset");
        this.turnsLeft = table.getActivePlayers().size() - 1;
    }
}
