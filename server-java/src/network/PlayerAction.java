package network;

public enum PlayerAction {
    CHECK("check"), FOLD("fold"), CALL("call"), ALL_IN("all_in");
    private final String action;
    PlayerAction(String action) {
        this.action = action;
    }

    public String getPlayerAction() {
        return action;
    }
}
