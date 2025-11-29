package org.poker.connection.messages;

import java.util.Map;
import java.util.Optional;

public interface MessageHandler {
    void sendMessage(Map<String, String> data);
    Optional<String> receiveMessage();
}
