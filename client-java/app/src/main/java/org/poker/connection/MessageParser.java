package org.poker.connection;

import java.util.HashMap;
import java.util.Map;

public class MessageParser {
    public static Map<String, String> parse(String msg) {
        Map<String, String> data = new HashMap<>();
        String[] lines = msg.split("\n");

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            int colonIndex = line.indexOf(':');
            if (colonIndex > 0) {
                String key = line.substring(0, colonIndex).trim();
                String value = line.substring(colonIndex + 1).trim();
                data.put(key, value);
            }
        }

        return data;

    }
}
