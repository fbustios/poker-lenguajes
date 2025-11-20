package poker.gamemodes;

import poker.dealing.Dealer;
import poker.items.PlayerModel;
import poker.pot.PotDistributer;

public final class HoldemPokerGamemode implements PokerGamemode {
    private final Dealer dealer;
    private HoldemRound currentRound;
    private PlayerModel currentPlayer;
    private PotDistributer pt;

    public HoldemPokerGamemode(Dealer dealer) {
        this.dealer = dealer;
    }

    public void start() {

    }

    public void play() {
        
    }

    @Override
    public boolean gamemodeOver() {
        return false;
    }
}
