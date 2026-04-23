package supplychaintrackingsystem;

public class Notification {
    private final String message;

    public Notification(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Notification{" + "message='" + message + '\'' + '}';
    }

    public String getMessage() {
        return message;
    }
}
