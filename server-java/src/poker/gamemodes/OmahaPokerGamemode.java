package poker.gamemodes;

import poker.dealing.DealingMethod;
import poker.items.Card;
import poker.items.Deck;
import poker.items.Player;
import poker.pot.Pot;
import poker.pot.PotDistributer;
import poker.rounds.HoldemRound;
import poker.rounds.TurnManager;

import java.util.List;
import java.util.Optional;

public class OmahaPokerGamemode implements PokerGamemode {
    private final Gamemode name = Gamemode.omaha;
    private final DealingMethod dealingMethod;
    private final List<HoldemRound> rounds;
    private int currentRound;
    private final TurnManager turnManager;
    private final PotDistributer potDistributer;
    private final Pot pot;
    private Deck deck;

    public OmahaPokerGamemode(DealingMethod dealingMethod, TurnManager turnManager, PotDistributer potDistributer, Pot pot, List<HoldemRound> rounds) {
        this.dealingMethod = dealingMethod;
        this.rounds = rounds;
        this.turnManager = turnManager;
        this.potDistributer = potDistributer;
        this.pot = pot;
    }
    private boolean checkAction() {
        return true;
    }


    @Override
    public boolean isOver() {
        return false;
    }

    @Override
    public void play(PokerAction lastPokerAction) {
        if (!checkAction()) {
            throw new IllegalStateException();
        }

        PlayerAction action = lastPokerAction.action();
        switch (action) {
            case RAISE -> handleRaise(lastPokerAction.player(),lastPokerAction.bet());
            case CALL -> handleCall(lastPokerAction.player(), lastPokerAction.bet());
            case CHECK -> handleCheck(lastPokerAction.player());
            case ALL_IN -> handleAllIn(lastPokerAction.player(), lastPokerAction.bet());
            case FOLD -> handleFold(lastPokerAction.player());
        }
    }

    @Override
    public Optional<Player> getNextTurn() {
        return turnManager.nextTurn();
    }

    @Override
    public Gamemode getName() {
        return Gamemode.omaha;
    }

    @Override
    public void distributePot() {
        List<Card> communityCards = dealingMethod.getCommunityCards();
        turnManager.getTable();
        if (!communityCards.isEmpty()) {

        }
        potDistributer.distribute();
    }

    @Override
    public String getDetails() {
        List<Card> community = dealingMethod.getCommunityCards();
        StringBuilder hand = new StringBuilder();
        for(int j = 0; j < community.size(); j++) {
            if (j < (community.size() - 1)) {
                hand.append(community.get(j).toString() + ",");
            } else {
                hand.append(community.get(j).toString());
            }

        }
        return turnManager.getDetails() + rounds.get(currentRound) +"\n" + "community_cards: " + hand + "\n" + "last_raise: " + pot.getLastRaise() + "\n";
    }

    @Override
    public boolean isRoundOver() {
        return turnManager.isRoundOver();
    }

    @Override
    public void deal() {
        dealingMethod.deal(turnManager.getTable(),deck);
    }

    @Override
    public void setStartingPlayer() {
        turnManager.setStartingPlayer();
    }
    private void handleRaise(Player player, int bet) {
        player.addMoney(-bet);
        pot.add(bet);
        pot.setLastBet(bet);
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
        pot.add(bet);
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
        pot.add(bet);
        turnManager.setPendingAction(false);
    }
}
