package com.poker.clientjava.repository.network;

import java.util.concurrent.CopyOnWriteArraySet;

public final class EventDispatcher {
    private final CopyOnWriteArraySet<EventListener> listeners = new CopyOnWriteArraySet<EventListener>();

    public void add(EventListener listener) {
        listeners.add(listener);
    }

    public void remove(EventListener listener) {
        listeners.remove(listener);
    }

    public void fireRaw(byte[] payload) {
        for (EventListener listener : listeners) {
            listener.onRawMessage(payload);
        }
    }

    public void fireText(String text) {
        for (EventListener listener : listeners) {
            listener.onTextMessage(text);
        }
    }

    public void fireConnected() {
        for (EventListener listener : listeners) {
            listener.onConnected();
        }
    }

    public void fireDisconnected(final Throwable throwable) {
        for (EventListener listener : listeners) {
            listener.onDisconnected();
        }
    }
}
