package network.control;

import network.io.EventEmitter;
import network.io.PlayerConnection;
import network.ServerEvent;
import network.ServerMessage;
import network.io.ClientMessage;
import poker.PokerGame;
import poker.items.PlayerModel;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public final class BlockingQueueController implements Controller{
    private final EventEmitter pokerEventEmitter;
    private final List<PlayerConnection> connections;
    private final BlockingQueue<ClientMessage> eventQueue;
    private final PokerGame game;


    public BlockingQueueController(EventEmitter pokerEventEmitter, List<PlayerConnection> connections, BlockingQueue<ClientMessage> eventQueue, PokerGame game) {
        this.pokerEventEmitter = pokerEventEmitter;
        this.connections = connections;
        this.eventQueue = eventQueue;
        this.game = game;
    }


    public void start() {
        ServerEvent startEvent = ServerEvent.GAME_STARTED;
        //falta clase que construya los mensajes del server;
        ServerMessage startMessage = new ServerMessage();
        sendEvent(startMessage);
        game.startGame();
        while(true) {
            try {
                Optional<ClientMessage> event = Optional.ofNullable(eventQueue.poll(100, TimeUnit.MILLISECONDS));
                if (game.isGamemodeOver()) {
                    ServerEvent serverEvent = ServerEvent.MODE_CHANGED; //sendEvent();
                }
                if (event.isPresent()) {
                    processEvent(event.get());
                    //sendEvent([valor retornado por processEvent]) que corresponde al round_update
                }
                if (game.isGameFinished()) {
                    PlayerModel winner = game.getWinner();
                    ServerEvent serverEvent = ServerEvent.GAME_ENDED;
                    //llama a sendEvent
                    return;
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }

        }
    }


    private void processEvent(final ClientMessage message) {
        //deberia de venir lo que hizo el jugador
        //currentGamemode.play(action) solo si el turnManager esperaba una accion
    }

    private void sendEvent(final ServerMessage message) {
        //primero parsear mensaje;
        String mes = "ms";
        pokerEventEmitter.emit(connections,mes);
    }
}
