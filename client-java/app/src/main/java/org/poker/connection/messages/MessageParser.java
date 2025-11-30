package org.poker.connection.messages;

import java.util.HashMap;
import java.util.Map;

public final class MessageParser {
    private MessageParser() {

    }

    public static Map<String, String> parse(String message) {
        Map<String, String> data = new HashMap<>();
        String[] lines = message.split("\n");

        int playerIndex = 0;

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }

            int colonIndex = line.indexOf(':');
            if (colonIndex <= 0) {
                continue;
            }

            String key = line.substring(0, colonIndex).trim();
            String value = line.substring(colonIndex + 1).trim();

            if (isPlayerData(value)) {
                parsePlayerData(data, key, value, playerIndex);
                playerIndex++;
            } else {
                data.put(key, value);
            }
        }

        return data;
    }

    public static void parsePlayerData(
            Map<String, String> data,
            String playerName,
            String value,
            int playerIndex
    ) {
        data.put("player_" + playerIndex, playerName);

        String[] parts = value.split(",");
        if (parts.length == 0) {
            return;
        }

        String money = parts[parts.length - 1].trim();
        data.put(playerName + "_money", money);

        if (parts.length > 1) {
            StringBuilder cards = new StringBuilder();
            for (int i = 0; i < parts.length - 1; i++) {
                if (i > 0) cards.append(", ");
                cards.append(parts[i].trim());
            }
            data.put(playerName + "_cards", cards.toString());
        }
    }

    public static int getPlayerCount(Map<String, String> data) {
        int count = 0;
        while (data.containsKey("player_" + count)) {
            count++;
        }
        return count;
    }

    public static String getPlayerName(Map<String, String> data, int index) {
        return data.get("player_" + index);
    }

    public static String getPlayerCards(Map<String, String> data, String playerName) {
        return data.get(playerName + "_cards");
    }

    public static String getPlayerMoney(Map<String, String> data, String playerName) {
        return data.get(playerName + "_money");
    }

    private static boolean isPlayerData(String value) {
        return value.contains(",");
    }
}
