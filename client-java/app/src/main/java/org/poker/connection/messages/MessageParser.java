package org.poker.connection.messages;

import java.util.HashMap;
import java.util.Map;

public final class MessageParser {
    private MessageParser() {

    }

    public static Map<String, String> parse(String msg) {
        final Map<String, String> data = new HashMap<>();
        final String[] lines = msg.split("\n");

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }

            final int colonIndex = line.indexOf(':');
            if (colonIndex > 0) {
                final String key = line.substring(0, colonIndex).trim();
                final String value = line.substring(colonIndex + 1).trim();
                data.put(key, value);
            }
        }

        return data;

    }
}
