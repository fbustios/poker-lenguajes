package poker;

import poker.gamemodes.PokerAction;
import poker.gamemodes.PokerGamemode;
import poker.items.PlayerModel;
import poker.table.PokerTable;

import java.util.List;
import java.util.Optional;

public class HorsePokerGame implements PokerGame{
    private final static int MIN_PLAYERS_TO_START = 2;
    private int currentGameIndex;
    private PokerGamemode currentGame;
    private final List<PokerGamemode> modes;
    private final PokerTable table;
    private boolean gameFinished;

    public HorsePokerGame(List<PokerGamemode> modes, PokerTable table) {
        this.modes = modes;
        this.gameFinished = false;
        this.table = table;
        this.currentGameIndex = 0;
    }

    @Override
    public void startGame() {
        if (table.getPlayers().size() < MIN_PLAYERS_TO_START) {
            throw new IllegalStateException("Se requieren al menos dos jugadores para iniciar el juego");
        }
        this.currentGame = this.modes.get(currentGameIndex);
    }


    @Override
    public Optional<PlayerModel> play(PokerAction lastPokerAction){
        if (!checkActionState(lastPokerAction)) {
            throw new IllegalStateException("Acción inválida");
        }
        currentGame.play(lastPokerAction);
        if(!currentGame.isOver()) {
            currentGame.distributePot();
            setNextMode();
        }

        return nextTurn();
    }

    @Override
    public boolean isGameFinished() {
        return this.gameFinished;
    }


    @Override
    public boolean isGamemodeOver() {
        return currentGame.isOver();
    }

    @Override
    public PlayerModel getWinner() {
        final List<PlayerModel> players = table.getPlayers();
        PlayerModel winner = new PlayerModel("t", List.of(),0);
        for (PlayerModel currentPlayer : players) {
            winner = currentPlayer.getMoney() > winner.getMoney() ? currentPlayer : winner;
        }
        return winner;
    }

    private Optional<PlayerModel> nextTurn() {
        return currentGame.getNextTurn();
    }

    private boolean checkActionState(PokerAction pokerAction) {
        final PlayerModel playerModel = pokerAction.player();
        final int playerMoney = playerModel.getMoney();
        final int actionBet = pokerAction.bet();
        return playerModel.isActive() && !playerModel.isAllIn() && (playerMoney >= actionBet);
    }

    private void setNextMode() {
        this.currentGameIndex +=1;
        this.currentGame = modes.get(currentGameIndex);
    }




}
