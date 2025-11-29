package poker.table;

import poker.items.Player;

import java.util.ArrayList;
import java.util.List;

public class CircularArrayPokerTable implements PokerTable {
    private final List<Player> players;
    private int currentPlayerIndex;

    public CircularArrayPokerTable(final List<Player> players) {
        this.players = players;
    }

    @Override
    public List<Player> getPlayers() {
        return players;
    }


    @Override
    public List<Player> getActivePlayers() {
        List<Player> activePlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.isActive()) {
                activePlayers.add(player);
            }
        }
        return activePlayers;
    }

    @Override
    public void resetTable() {
        for (Player player : players) {
            if (player.isActive()) {
                player.setFolded(false);
                player.setActive(true);
                player.setAllIn(false);
            }
        }
    }

    public int nextActivePlayerIndex(final int i) {
        int currentIndex = i;
        do {
            currentIndex = (currentIndex + 1) % players.size();
        } while (!players.get(currentIndex).isActive());
        return currentIndex;
    }

    @Override
    public void setCurrentPlayer(int i) {
        this.currentPlayerIndex = i;
    }

    @Override
    public Player next() {
        do {
            this.currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } while (!players.get(currentPlayerIndex).isActive());
        return players.get(currentPlayerIndex);
    }
}
