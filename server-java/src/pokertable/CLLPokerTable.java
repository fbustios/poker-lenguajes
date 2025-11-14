package pokertable;

public final class CLLPokerTable implements PokerTable{
    private CLLPokerTable next;


//basura tengo que hacer un nodo individual y luego
    private CLLPokerTable() {

    }

    @Override
    public PokerTable next() {
        return this.next;
    }
}
