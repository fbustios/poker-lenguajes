import network.control.BlockingQueueController;
import network.control.ConnectionPlayerMapping;
import network.control.Controller;
import network.control.MapPlayerMapping;
import network.io.*;
import poker.HorsePokerGame;
import poker.PokerGame;
import poker.dealing.DealingMethod;
import poker.dealing.HoldemDealingMethod;
import poker.dealing.OmahaDealingMethod;
import poker.gamemodes.HoldemPokerGamemode;
import poker.gamemodes.OmahaPokerGamemode;
import poker.gamemodes.PokerGamemode;
import poker.handranking.CProgramRankingSystem;
import poker.handranking.RankingSystem;
import poker.items.Deck;
import poker.items.ListDeck;
import poker.items.Player;
import poker.pot.HiPotDistributer;
import poker.pot.MapPot;
import poker.pot.Pot;
import poker.pot.PotDistributer;
import poker.rounds.HoldemOmahaTurnManager;
import poker.rounds.HoldemRound;
import poker.rounds.TurnManager;
import poker.table.CircularArrayPokerTable;
import poker.table.PokerTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public final class ComponentFactory {


    public static Controller buildController(final Map<Connection, Player> connections,
                                             final BlockingQueue<ClientMessage> queue) {
        ConnectionPlayerMapping mapping = MapPlayerMapping.build(connections);
        EventEmitter eventEmitter = new PokerEventEmitter();
        PokerGame game = buildHorsePokerGame(mapping.getPlayers(), mapping);
        Controller controller = new BlockingQueueController(eventEmitter, queue, game, mapping);
        return controller;
    }
    private static PokerGame buildHorsePokerGame(List<Player> players, ConnectionPlayerMapping mapping) {
        List<PokerGamemode> modes = new ArrayList<>();
        final Pot pot = buildPot();
        final Deck deck = buildDeck();
        final PokerTable table = buildPokerTable(players);
        final PokerGamemode holdem = buildHoldem(table, pot, deck,  mapping);
        final PokerGamemode holdem2 = buildHoldem(table, pot, deck,  mapping);
        final PokerGamemode holdem3 = buildHoldem(table, pot, deck,  mapping);
        final PokerGamemode holdem4 = buildHoldem(table, pot, deck,  mapping);
        final PokerGamemode holdem5 = buildHoldem(table, pot, deck,  mapping);
        final PokerGamemode omaha = buildOmaha(table, pot, deck, mapping);
        modes.addFirst(holdem);
        modes.addLast(holdem2);
        modes.addLast(holdem3);
        modes.addLast(holdem5);
        modes.addLast(buildRazz(table,pot,deck));
        return new HorsePokerGame(modes, table, holdem, pot);
    }

    private static PokerGamemode buildHoldem(PokerTable table, Pot pot, Deck deck, ConnectionPlayerMapping mapping) {
        final DealingMethod dealingMethod = new HoldemDealingMethod();
        List<HoldemRound> rounds = new ArrayList<>();
        rounds.addFirst(HoldemRound.PRE_FLOP);
        rounds.addLast(HoldemRound.FLOP);
        rounds.addLast(HoldemRound.TURN);
        rounds.addLast(HoldemRound.RIVER);
        final TurnManager turnManager = new HoldemOmahaTurnManager(rounds, table);
        final PotDistributer potDistributer = buildHiPotDistributer(pot, buildRankingSystem(mapping),table);
        List<HoldemRound> rounds1 = new ArrayList<>(rounds);
        rounds1.addLast(HoldemRound.SHOWDOWN);

        return new HoldemPokerGamemode(dealingMethod, turnManager, potDistributer, pot, buildDeck(), rounds);
    }

    private static PokerGamemode buildOmaha(PokerTable table, Pot pot, Deck deck, ConnectionPlayerMapping map) {
        final DealingMethod dealingMethod = new OmahaDealingMethod();
        List<HoldemRound> rounds = new ArrayList<>();
        rounds.addFirst(HoldemRound.PRE_FLOP);
        rounds.addLast(HoldemRound.FLOP);
        rounds.addLast(HoldemRound.TURN);
        rounds.addLast(HoldemRound.RIVER);
        final TurnManager turnManager = new HoldemOmahaTurnManager(rounds, table);
        final PotDistributer potDistributer = buildHiPotDistributer(pot, buildRankingSystem(map),table);

        return new OmahaPokerGamemode(dealingMethod, turnManager, potDistributer, pot, rounds);
    }

    private static PokerGamemode buildRazz(PokerTable table, Pot pot, Deck deck) {
        return null;
    }

    private static PotDistributer buildHiPotDistributer(Pot pot, RankingSystem rankingSystem, PokerTable table) {
        return new HiPotDistributer(rankingSystem, pot, table);
    }

    private static PokerTable buildPokerTable(List<Player> players) {
        return new CircularArrayPokerTable(players);
    }

    private static RankingSystem buildRankingSystem(ConnectionPlayerMapping mp) {
        return new CProgramRankingSystem(mp);
    }

    private static Pot buildPot() {
        return new MapPot();
    }

    private static Deck buildDeck() {
        return ListDeck.build();
    }
    //deberia ser un factory entero de gamemodes pero bueno xd


}
