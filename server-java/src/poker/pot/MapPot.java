package poker.pot;

import poker.items.Player;

import java.util.HashMap;
import java.util.Map;

public class MapPot implements Pot{
    private Map<Player, Integer> pots = new HashMap<>();
    private int mainAmount = 0;

    @Override
    public void add(int amount) {

        this.mainAmount += amount;
    }

    @Override
    public int getAmount() {
        return mainAmount;
    }

    @Override
    public int takeAll() {
        final int total = this.mainAmount;
        this.mainAmount = 0;
        this.pots.clear();
        return total;
    }

    @Override
    public void setSidePots(Player player, int amount) {
        int currentAmount = this.mainAmount();
        if (amount >= currentAmount){
            return;
        }
        int sidePotAmount = 0;
        
    }
    @Override
    public boolean isEmpty() {
        return mainAmount == 0;
    }
}
