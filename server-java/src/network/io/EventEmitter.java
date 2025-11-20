package network.io;

import java.util.List;

public interface EventEmitter {
    void emit(List<PlayerConnection> connectionsList, String message);
}
