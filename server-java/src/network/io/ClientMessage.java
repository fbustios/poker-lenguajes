package network.io;

import java.util.Map;

public record ClientMessage(ClientEvent event, Map<String, String> details) {
}
