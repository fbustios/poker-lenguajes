package poker.gamemodes;

import poker.dealing.Dealer;
import poker.items.PlayerModel;
import poker.pot.PotDistributer;

import java.util.Optional;

public final class HoldemPokerGamemode implements PokerGamemode {
    private final Dealer dealer;
    private HoldemRound currentRound;
    private PlayerModel currentPlayer;
    private PotDistributer pt;

    public HoldemPokerGamemode(Dealer dealer) {

        this.dealer = dealer;
    }

    @Override
    public boolean isOver() {
        return false;
    }

    @Override
    public void play(Action lastAction) {
        if (!this.gameStarted) {
            start();
        } else {

        }
    }

    @Override
    public Optional<PlayerModel> getNextTurn() {
        return Optional.empty();
    }

    private void start() {
        dealer.deal();
    }

    private boolean checkAction() {}

}
