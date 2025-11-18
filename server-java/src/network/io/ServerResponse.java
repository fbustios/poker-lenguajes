package network.io;

import network.ServerEvent;

import java.util.Map;

public record ServerResponse(ServerEvent event, Map<String, String> headers) {
}
