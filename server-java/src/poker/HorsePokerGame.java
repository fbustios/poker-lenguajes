package poker;

import poker.gamemodes.PokerGamemode;
import poker.table.PokerTable;

public class HorsePokerGame implements PokerGame{
    private PokerGamemode currentGame;
    private final PokerTable table;
    private int currentPot;

    public HorsePokerGame(PokerTable table) {
        this.table = table;
    }

    @Override
    public boolean isGameFinished() {
        return false;
    }

    @Override
    public boolean isGamemodeOver() {
        return false;
    }

    public void play(){

    }

}
