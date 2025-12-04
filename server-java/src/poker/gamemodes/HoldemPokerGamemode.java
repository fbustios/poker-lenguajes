package poker.gamemodes;

import poker.dealing.DealingMethod;
import poker.items.Card;
import poker.items.Deck;
import poker.items.Player;
import poker.pot.Pot;
import poker.pot.PotDistributer;
import poker.rounds.HoldemRound;
import poker.rounds.TurnManager;
import poker.table.PokerTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class HoldemPokerGamemode implements PokerGamemode {
    private final Gamemode name = Gamemode.holdem;
    private final DealingMethod dealingMethod;
    private int currentRound;
    private List<HoldemRound> rounds;
    private final TurnManager turnManager;
    private final PotDistributer potDistributer;
    private final Pot pot;
    private Deck deck;

    public HoldemPokerGamemode(final DealingMethod dealingMethod,
                               final TurnManager turnManager,
                               final PotDistributer pt,
                               final Pot pot,
                               final Deck deck,
                               final List<HoldemRound> rounds) {
        this.pot = pot;
        this.rounds = rounds;
        currentRound = 0;
        pot.setLastBet(100);
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
        switch (action) {
            case RAISE -> handleRaise(lastPokerAction.player(),lastPokerAction.bet());
            case CALL -> handleCall(lastPokerAction.player(), lastPokerAction.bet());
            case CHECK -> handleCheck(lastPokerAction.player());
            case ALL_IN -> handleAllIn(lastPokerAction.player(), lastPokerAction.bet());
            case FOLD -> handleFold(lastPokerAction.player());
        }
    }

    @Override
    public boolean isOver() {
        return turnManager.isGameOver();
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
        List<Card> communityCards = dealingMethod.getCommunityCards();
        PokerTable table = turnManager.getTable();
        if (!communityCards.isEmpty()) {
            List<Player> players = table.getActivePlayers();
            for(Player p: players) {
                p.setCards(Stream.concat(p.getCards().stream(), communityCards.stream())
                        .collect(Collectors.toCollection(ArrayList::new)));
            }
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

    private boolean checkAction() {
        return true;
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
