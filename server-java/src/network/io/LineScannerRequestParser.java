package network.io;

import network.ClientEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class LineScannerRequestParser implements RequestParser {
    private final static int DETAILS_SIZE = 8;
    @Override
    public Optional<ClientMessage> parse(final String event) {
        final String[] parts = event.split("\n",3);
        if (parts.length != 3) {
            return Optional.empty();
        }
        final String action = parts[0];
        final String author = parts[1];
        final String details = parts[2];
        System.out.println(parts[2]);
        final Optional<ClientEvent> parsedEvent = parseEvent(action);
        System.out.println("llegue1");
        final Optional<Map<String, String>> parsedDetails = parseDetails(details);
        System.out.println("llegue2");
        final Optional<String> parsedAuthor = parseAuthor(author);
        System.out.println("llegue3");
        if (parsedEvent.isEmpty() || parsedDetails.isEmpty() || parsedAuthor.isEmpty()) {
            return Optional.empty();
        }
        System.out.println("llegue4");
        ClientMessage parsedMessage = new ClientMessage(parsedEvent.get(), parsedAuthor.get() ,parsedDetails.get()); //corregir creo que ya
        System.out.println("llegue5");
        return Optional.of(parsedMessage);
    }
    private Optional<String> parseAuthor(final String line){
        final String[] parts = line.split(" ");
        if (parts.length != 2) {
            return Optional.empty();
        }
        final String author = parts[1];
        return Optional.of(author);
    }

    private Optional<ClientEvent> parseEvent(final String firstLine) {

        final String[] parts = firstLine.split(" ");
        if (parts.length != 2) {
            return Optional.empty();
        }
        final String event = parts[1];
        if (Objects.equals(event, "join_game")) return Optional.of(ClientEvent.JOIN_GAME);
        if (Objects.equals(event, "action")) return Optional.of(ClientEvent.ACTION);
        return Optional.empty();
    }

    private Optional<Map<String, String>> parseDetails(String details) {
        final Map<String, String> parsedDetails = new HashMap<>();
        final String[] parts = details.split("\n");
        int currentLineIndex = 1;
        String currentLine;
        while (currentLineIndex < parts.length && !(parts[currentLineIndex]).isEmpty()) {
            currentLine = parts[currentLineIndex];
            final String[] currentLineParts = currentLine.split(": ", 2);
            if (currentLineParts.length != 2) {
                return Optional.empty();
            }
            final String key = currentLineParts[0].trim();
            final String value = currentLineParts[1].trim();
            parsedDetails.put(key, value);
            currentLineIndex++;
        }
        return Optional.of(parsedDetails);
    }
}
