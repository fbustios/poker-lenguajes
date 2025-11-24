package network.io;

import java.util.List;

public interface EventEmitter {
    void emit(List<Connection> connectionsList, String message);
}
