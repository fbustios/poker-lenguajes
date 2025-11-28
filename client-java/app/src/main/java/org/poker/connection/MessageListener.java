package org.poker.connection;

import com.google.gson.JsonObject;

public interface MessageListener {
    void onMessageReceived(String event, JsonObject message);
}
