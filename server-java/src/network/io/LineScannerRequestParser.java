package network.io;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class LineScannerRequestParser implements RequestParser {
    private final static int DETAILS_SIZE = 8;
    @Override
    public Optional<ClientMessage> parse(final String event) {
        final String[] parts = event.split("\n");
        if (parts.length != 2) {
            return Optional.empty();
        }
        final String action = parts[0];
        final String details = parts[1];
        final Optional<ClientEvent> parsedEvent = parseEvent(action);
        final Optional<Map<String, String>> parsedDetails = parseDetails(details);
        if (parsedEvent.isEmpty() || parsedDetails.isEmpty()) {
            return Optional.empty();
        }
        ClientMessage parsedMessage = new ClientMessage(parsedEvent.get(), parsedDetails.get());
        return Optional.of(parsedMessage);
    }

    private Optional<ClientEvent> parseEvent(final String firstLine) {
        final String[] parts = firstLine.split(" ");
        if (parts.length != 2) {
            return Optional.empty();
        }
        final String event = parts[1];
        System.out.println(event);
        if (Objects.equals(event, "join_game")) return Optional.of(ClientEvent.JOIN_GAME);
        if (Objects.equals(event, "action")) return Optional.of(ClientEvent.ACTION);
        return Optional.empty();
    }

    private Optional<Map<String, String>> parseDetails(String details) {
        final Map<String, String> parsedDetails = new HashMap<>();
        final String[] parts = details.split("\n");
        if(parts.length != DETAILS_SIZE) return Optional.empty();
        while () {

        }
        return Optional.of(parsedDetails);
    }
}
