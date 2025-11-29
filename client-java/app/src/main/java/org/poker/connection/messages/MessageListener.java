package org.poker.connection.messages;

import java.util.Map;

public interface MessageListener {
    void onMessageReceived(String event, Map<String, String> message);
}
