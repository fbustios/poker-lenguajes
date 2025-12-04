package poker;

import poker.gamemodes.Gamemode;
import poker.gamemodes.PokerAction;
import poker.gamemodes.PokerGamemode;
import poker.items.Player;
import poker.pot.Pot;
import poker.table.PokerTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HorsePokerGame implements PokerGame, GameState{
    private final static int MIN_PLAYERS_TO_START = 0;
    private int currentGameIndex;
    private PokerGamemode currentGame;
    private List<PokerGamemode> modes;
    private final PokerTable table;
    private final Pot pot;


    public HorsePokerGame(List<PokerGamemode> modes, PokerTable table, PokerGamemode gamemode, Pot pot) {
        this.modes = modes;
        this.table = table;
        this.pot = pot;
        this.currentGameIndex = 0;
        this.currentGame = gamemode;
    }

    @Override
    public void startGame() {
        if (table.getPlayers().size() < MIN_PLAYERS_TO_START) {
            System.out.println("no puedo empezar");
        }
        try {
            currentGame.deal();
        } catch (Exception e) {
            System.out.println("me meti");
            System.out.println(e.getMessage());
            System.out.println(e.fillInStackTrace());
            System.out.println(e.getCause());
            System.out.println(e.getStackTrace());

        }

        this.currentGame = this.modes.get(currentGameIndex);
    }


    @Override
    public void play(PokerAction lastPokerAction){
        if (!checkActionState(lastPokerAction)) {
            throw new IllegalStateException("Acción inválida");
        }
        currentGame.play(lastPokerAction);
        if(currentGame.isRoundOver() && !currentGame.isOver()) {
            currentGame.deal();
            System.out.println("im dealing");
        }

        if(currentGame.isOver()) {
            System.out.println("termino el modo, pasando al siguiente");
            currentGame.distributePot();
            //table.resetTable();
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
        Player winner = new Player("t", new ArrayList<>(),0);
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
        System.out.println(playerMoney + " > " + actionBet);
        return player.isActive() && !player.isAllIn() && (playerMoney >= actionBet);
    }
    @Override
    public void setNextMode() {
        if (currentGameIndex < (modes.size()-1)) {
            this.currentGameIndex +=1;
            this.currentGame = modes.get(currentGameIndex);
            table.resetTable();
            //this.currentGame.setStartingPlayer();
        }
    }


    @Override
    public List<Player> getPlayers() {

        return table.getActivePlayers();
    }

    @Override
    public int getPot() {
        return pot.getAmount();
    }

    @Override
    public Gamemode getCurrentGamemode() {
        return currentGame.getName();
    }

    @Override
    public String getDetails() {
        return currentGame.getDetails();
    }
}
