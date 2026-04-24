package supplychaintrackingsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationService {
    private int notificationID;
    private final List<Notification> notifications = new ArrayList<>();
    private final List<Notification> adminNotifications = new ArrayList<>();

    public void sendNotification(int userID, String message) {
        if (message == null || message.isBlank()) {
            System.out.println("Notification message cannot be empty.");
            return;
        }

        Notification notification = new Notification("User " + userID + ": " + message);
        notifications.add(notification);
        System.out.println("Notify user " + userID + ": " + message);
    }

    public void notifyAdmin(String message) {
        if (message == null || message.isBlank()) {
            System.out.println("Notification message cannot be empty.");
            return;
        }

        Notification notification = new Notification("Admin: " + message);
        adminNotifications.add(notification);
        System.out.println("Notify admin: " + message);
    }

    public int getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }

    public List<Notification> getNotifications() {
        return Collections.unmodifiableList(notifications);
    }

    public List<Notification> getAdminNotifications() {
        return Collections.unmodifiableList(adminNotifications);
    }
}
