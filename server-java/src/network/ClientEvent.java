package network;

public enum ClientEvent {
    JOIN_GAME("join_game"), ACTION("action");
    private final String action;

    ClientEvent(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
