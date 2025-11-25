package com.poker.clientjava.repository.network;

public interface NetworkClient {
    void connect(String host, int port);
    void disconnect();
    boolean isConnected();
    void sendText(String text);
    void sendBytes(byte[] bytes);
}
