package network.io;

import java.util.Optional;

public interface RequestParser {
    Optional<ClientMessage> parse(final String event);
}
