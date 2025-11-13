package com.poker.clientjava.Repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public final class NetworkClient {
    private final String host;
    private int port;
    private Socket socket;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final BufferedReader reader;
    private final BufferedWriter writer;

    private final Consumer<String> onMessage;

    public NetworkClient(String host, int port, Consumer<String> onMessage) {
        this.host = host;
        this.port = port;
        this.onMessage = onMessage;
    }

    private void connect() {
        try {
            socket = new Socket(host, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
