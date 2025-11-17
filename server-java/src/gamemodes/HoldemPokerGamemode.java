package gamemodes;

import dealing.Dealer;
import pokeritems.PlayerModel;

public final class HoldemPokerGamemode implements PokerGamemode {
    private final Dealer dealer;
    private HoldemRound currentRound;
    private PlayerModel currentPlayer;

    public HoldemPokerGamemode(Dealer dealer) {
        this.dealer = dealer;
    }

    public void start() {

    }

    public void play() {
        
    }


}
