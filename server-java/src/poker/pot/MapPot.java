package poker.pot;

import poker.items.Player;

import java.util.*;

public class MapPot implements Pot{
    private int totalAmount = 0;
    private int lastBet = 0;

    private final Map<Player, Integer> contributions = new HashMap<>();

    private final List<Map<Player, Integer>> sidePots = new ArrayList<>();

    @Override
    public void add(int amount) {

        this.totalAmount += amount;
    }

    public void addContribution(Player player, int amount) {
        totalAmount += amount;
        int currentAmount = this.contributions.getOrDefault(player, 0);

        this.contributions.put(player, currentAmount + amount);
    }

    @Override
    public int getAmount() {
        return totalAmount;
    }
    @Override
    public int getLastRaise() {
        return lastBet;
    }

    @Override
    public void setLastBet(int value) {
        this.lastBet = value;
    }

    @Override
    public int takeAll() {
        final int total = this.totalAmount;
        this.totalAmount = 0;
        this.contributions.clear();
        this.sidePots.clear();
        return total;
    }

    public List<Map<Player, Integer>> getSidePots() {
        return sidePots;
    }

    @Override
    public void setSidePots(Player allInPlayer, int amount) {
        addContribution(allInPlayer, amount);
        rebuildSidePots();
        
    }

    private void rebuildSidePots() {
        sidePots.clear();
        List<Integer> levels = new ArrayList<>();
        for(Integer amount : contributions.values()) {
            if(amount > 0 &&!levels.contains(amount)) {
                levels.add(amount);
            }
        }
        if(levels.isEmpty()) {
            return;
        }
        Collections.sort(levels);

        int previousLevel = 0;

        for(int i = 0; i < levels.size(); i++) {
            int level = levels.get(i);

            List<Player> playersInPot = new ArrayList<>();
            for(Map.Entry<Player, Integer> entry : contributions.entrySet()) {
                Player player = entry.getKey();
                int contribution = entry.getValue();
                if(contribution >= level) {
                    playersInPot.add(player);
                }
            }
            if(playersInPot.isEmpty()){
                previousLevel = level;
                continue;
            }
            int diff = level - previousLevel;
            if (diff <= 0){
                previousLevel = level;
                continue;
            }
            Map<Player, Integer> potMap = new HashMap<>();
            for(int j = 0; j < playersInPot.size(); j++) {
                Player p = playersInPot.get(j);
                potMap.put(p, diff);
            }
            sidePots.add(potMap);
            previousLevel = level;
        }
    }

    @Override
    public boolean isEmpty() {
        return totalAmount == 0;
    }
}
