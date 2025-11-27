package poker.pot;

public class Pot {
    private int amount = 0;

    public void add(int amount) {
        this.amount += amount;
    }

    public int getAmount() {
        return amount;
    }

    public int takeAll() {
        final int total = this.amount;
        this.amount = 0;
        return total;
    }
    public boolean isEmpty() {
        return amount == 0;
    }
}
