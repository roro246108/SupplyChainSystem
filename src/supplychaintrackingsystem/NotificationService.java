package supplychaintrackingsystem;

public class NotificationService {
    public void sendNotification(int userID, String message) {
        System.out.println("Notify user " + userID + ": " + message);
    }

    public void notifyAdmin(String message) {
        System.out.println("Notify admin: " + message);
    }
}
