package network.io;

import network.ClientEvent;

import java.util.Map;

public record ClientMessage(ClientEvent event, Map<String, String> details) {
}
