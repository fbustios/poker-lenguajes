package network.io;

public interface Connection extends Runnable {
    boolean isAlive();
    void setAlive(boolean value);
    void addToOutputQueue(String event);
    void run();
}
