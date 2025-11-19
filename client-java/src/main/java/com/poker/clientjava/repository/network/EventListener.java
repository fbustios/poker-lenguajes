package com.poker.clientjava.repository.network;

public interface EventListener {
    void onTextMessage(String text);
    void onRawMessage(byte[] payload);
    void onConnected();
    void onDisconnected();
}
