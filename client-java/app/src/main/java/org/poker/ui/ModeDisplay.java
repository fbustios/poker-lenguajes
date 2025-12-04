package org.poker.ui;

import java.util.Map;

public interface ModeDisplay {
    void showDisplay();

    void hideDisplay();

    void updateState(Map<String, String> message);

    void setLocalPlayerName(String name);
}