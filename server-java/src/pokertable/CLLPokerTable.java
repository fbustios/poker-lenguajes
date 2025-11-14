package pokertable;

public final class CLLPokerTable implements PokerTable{
    private PokerTable next;
    private final String playerName;


    private CLLPokerTable(final String playerName) {
        this.playerName = playerName;
    }

    @Override
    public PokerTable next() {
        return this.next;
    }
}
