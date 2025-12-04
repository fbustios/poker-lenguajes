package poker.rounds;

import poker.items.Card;
import poker.items.Player;
import poker.table.PokerTable;

import java.util.List;
import java.util.Optional;

import static java.lang.Math.min;

public class RazzTurnManager implements TurnManager {
    private List<Street> rounds;
    private int currentStreet;
    private final PokerTable table;
    boolean pendingAction;
    int turnsLeft;

    public RazzTurnManager(final PokerTable table, final List<Street> rounds) {
        this.table = table;
        this.rounds = rounds;
        this.currentStreet = 0;
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
            setStartingPlayer();
        }
        if (currentStreet == rounds.size() - 1) {
            System.out.println("gamemode ended");
            return Optional.empty();
        }
        this.turnsLeft--;
        this.pendingAction = true;
        return Optional.of(table.next());
    }

    @Override
    public void setStartingPlayer() {
        List<Player> players = table.getPlayers();
        int starter = 0;
        int best = 15;
        for(int i = 0; i < players.size(); i++) {
            List<Card> cards = players.get(i).getCards();
            int minCard = getMinCardFromHand(cards);
            if(minCard < best) {
                starter = i;
                best = minCard;
            }
        }
        table.setCurrentPlayer((starter - 1) % 2);
    }

    private int getMinCardFromHand(List<Card> cards) {
        int minCard = 0;
        for (Card card : cards) {
            if(card.seen()) {
                int value = card.rank().getValue();
                if (value == 14) value = 1;
                minCard = min(minCard, value);
            }
        }
        return minCard;
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

    @Override
    public String getDetails() {
        return "";
    }
}
