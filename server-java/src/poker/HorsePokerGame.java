package poker;

import poker.gamemodes.Action;
import poker.gamemodes.PokerGamemode;
import poker.items.PlayerModel;
import poker.table.PokerTable;

import java.util.List;
import java.util.Optional;

public class HorsePokerGame implements PokerGame{
    private int currentGameIndex;
    private PokerGamemode currentGame;
    private List<PokerGamemode> modes;
    private final PokerTable table;
    private boolean gameFinished;

    public HorsePokerGame(PokerTable table) {
        this.gameFinished = false;
        this.table = table;
        this.currentGameIndex = 0;
    }
    public void startGame() {
        if (table.getPlayers().size() < 2) {
            throw new IllegalStateException("Se requieren al menos dos jugadores para iniciar el juego.");
        }
        this.currentGame = this.modes.get(currentGameIndex);
    }


    @Override
    public Optional<PlayerModel> play(Action lastAction){
        currentGame.makeAction(lastAction);
        if(!currentGame.isOver()) {
            setNextMode();
        }
        Optional<PlayerModel> nextPlayer = currentGame.getNextTurn();
        return nextPlayer;
    }

    @Override
    public boolean isGameFinished() {
        return this.gameFinished;
    }


    @Override
    public boolean isGamemodeOver() {
        return currentGame.gamemodeOver();
    }

    @Override
    public Optional<List<PlayerModel>> getWinners() {
        List<PlayerModel> players = table.getPlayers();
        //recorrer jugadores para ver quienes tienen mas dinero;
        return Optional.empty();
    }

    private boolean checkActionState(Action action) {
        PlayerModel playerModel = action.player();
        if (playerModel.) {

        }
    }

    private void setNextMode() {
        this.currentGameIndex +=1;
        this.currentGame = modes.get(currentGameIndex);
    }




}
