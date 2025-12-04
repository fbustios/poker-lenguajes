package poker;

import poker.gamemodes.Gamemode;
import poker.gamemodes.PokerAction;
import poker.gamemodes.PokerGamemode;
import poker.items.Player;
import poker.table.PokerTable;
import java.util.List;
import java.util.Optional;

public class HorsePokerGame implements PokerGame, GameState{
    private final static int MIN_PLAYERS_TO_START = 0;
    private int currentGameIndex;
    private PokerGamemode currentGame;
    private final List<PokerGamemode> modes;
    private final PokerTable table;

    public HorsePokerGame(List<PokerGamemode> modes, PokerTable table) {
        this.modes = modes;
        this.table = table;
        this.currentGameIndex = 0;
    }

    @Override
    public void startGame() {
        if (table.getPlayers().size() < MIN_PLAYERS_TO_START) {
            System.out.println("no puedo empezar");
        }
        this.currentGame = this.modes.get(currentGameIndex);
    }


    @Override
    public void play(PokerAction lastPokerAction){
        if (!checkActionState(lastPokerAction)) {
            throw new IllegalStateException("Acción inválida");
        }
        currentGame.play(lastPokerAction);
        System.out.println("hello");
        if(currentGame.isOver()) {
            currentGame.distributePot();
            setNextMode();
        }
    }

    @Override
    public boolean isGameFinished() {
        return currentGame.isOver() && (currentGameIndex == modes.size() - 1);
    }


    @Override
    public boolean isGamemodeOver() {
        return currentGame.isOver();
    }

    @Override
    public Player getWinner() {
        final List<Player> players = table.getPlayers();
        Player winner = new Player("t", List.of(),0);
        for (Player currentPlayer : players) {
            winner = currentPlayer.getMoney() > winner.getMoney() ? currentPlayer : winner;
        }
        return winner;
    }

    @Override
    public Optional<Player> nextTurn() {
        return currentGame.getNextTurn();
    }

    @Override
    public GameState getGameState() {
        return this;
    }

    private boolean checkActionState(PokerAction pokerAction) {
        final Player player = pokerAction.player();
        final int playerMoney = player.getMoney();
        final int actionBet = pokerAction.bet();
        return player.isActive() && !player.isAllIn() && (playerMoney >= actionBet);
    }

    private void setNextMode() {
        if (currentGameIndex < (modes.size() - 1)) {
            this.currentGameIndex +=1;
            this.currentGame = modes.get(currentGameIndex);
        }

    }


    @Override
    public List<Player> getPlayers() {
        return table.getActivePlayers();
    }

    @Override
    public int getPot() {
        return 0;
    }

    @Override
    public Gamemode getCurrentGamemode() {
        return currentGame.getName();
    }
}
