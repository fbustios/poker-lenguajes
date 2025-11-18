package network.control;

import poker.gamemodes.PokerGamemode;
import poker.table.PokerTable;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class BlockingQueueController implements Controller{
    private final BlockingQueue<String> eventQueue;
    private PokerGamemode currentGamemode;
    private final PokerTable table;
    private boolean isGameFinished;

    public BlockingQueueController(BlockingQueue<String> eventQueue, PokerTable table) {
        this.eventQueue = eventQueue;
        this.table = table;
    }


    public void start() {
        while(true) {
            try {
                Optional<String> event = Optional.ofNullable(eventQueue.poll(100, TimeUnit.MILLISECONDS));
                if (event.isPresent()) {
                    processEvent();
                }
                if (isGameFinished) {
                    return;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }

        }
    }

    private void processEvent() {
        //deberia de venir lo que hizo el jugador
        //currentGamemode.play(action) solo si el turnManager esperaba una accion
    }
}
