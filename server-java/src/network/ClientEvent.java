package network;

public enum ClientEvent {
    JOIN_GAME("join_game"), ACTION("action"), LEAVE_GAME("leave_game");
    private final String action;

    ClientEvent(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
