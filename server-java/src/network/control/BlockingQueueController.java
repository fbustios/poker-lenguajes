package network.control;

import poker.PokerGame;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public final class BlockingQueueController implements Controller{
    private final BlockingQueue<String> eventQueue;
    private final PokerGame game;


    public BlockingQueueController(BlockingQueue<String> eventQueue, PokerGame game) {
        this.eventQueue = eventQueue;
        this.game = game;
    }


    public void start() {
        while(true) {
            try {
                Optional<String> event = Optional.ofNullable(eventQueue.poll(100, TimeUnit.MILLISECONDS));
                if (event.isPresent()) {
                    processEvent();
                }
                if (game.isGameFinished()) {
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
