package poker.pot;

import poker.handranking.RankingSystem;
import poker.items.Player;
import poker.table.PokerTable;

import java.util.List;

public class HiPotDistributer implements PotDistributer{

    private final RankingSystem rankingSystem;
    private final PokerTable table;
    private final MapPot pot;

    public HiPotDistributer(final RankingSystem rankingSystem,
                            final MapPot pot,
                            final PokerTable table) {
        this.rankingSystem = rankingSystem;
        this.pot = pot;
        this.table = table;
    }

    @Override
    public List<Player> distribute() {

        final List<Player> activePlayers = table.getActivePlayers();

        if (activePlayers.isEmpty() || pot.isEmpty()) {return List.of();}

        final List<Player> ranked = rankingSystem.rank(activePlayers);
        final Player winner = ranked.get(0);

        final int totalAmount = pot.takeAll();
        winner.addMoney(totalAmount);

        return List.of(winner);
    }
}

