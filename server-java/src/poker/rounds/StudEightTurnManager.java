package poker.rounds;

import poker.items.Card;
import poker.items.Player;
import poker.table.PokerTable;

import java.util.List;
import java.util.Optional;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class StudEightTurnManager implements TurnManager{
    private List<Street> rounds;
    private final int currentStreet;
    private final PokerTable table;
    boolean pendingAction;
    int turnsLeft;

    public StudEightTurnManager(final PokerTable table, final List<Street> rounds) {
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
        List<Player> players = table.getActivePlayers();
        if (currentStreet > 0) {
            int min = 15;
            int starter = 0;
            for (int i = 0; i < players.size(); i++) {
                int minCard = getMinCardFromHand(players.get(i).getCards());
                if(minCard < min) {
                    min = minCard;
                    starter = i;
                }
            }
            table.setCurrentPlayer((starter + 1) % 2);
        } else {
            int max = 0;
            int starter = 0;
            for(int i = 0; i < players.size(); i++) {
                int maxCard = getMaxCardFromHand(players.get(i).getCards());
                if (maxCard > max) {
                    max = maxCard;
                    starter = i;
                }
            }
            table.setCurrentPlayer((starter + 1) % 2);
        }
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

    private int getMaxCardFromHand(List<Card> cards) {
        int maxCard = 0;
        for (Card card : cards) {
            if(card.seen()) {
                int value = card.rank().getValue();
                if (value == 14) value = 1;
                maxCard = max(maxCard, value);
            }
        }
        return maxCard;
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
