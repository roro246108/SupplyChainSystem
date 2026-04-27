/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package supplychaintrackingsystem;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rawan
 */
public class AdminBoundary {
    
     private UserController controller;

    public AdminBoundary(UserController controller) {
        this.controller = controller;
    }

    public void manageUsers(User user) {
        controller.createUser(user);
    }

    public void updateUser(int userID, String data) {
        controller.updateUser(userID, data);
    }

    public void deleteUser(int userID) {
        controller.deleteUser(userID);
    }

    public void sendNotification(String message) {
        if (message == null || message.isBlank()) {
            System.out.println("Notification message is empty.");
            return;
        }

        for (User user : controller.getUsers()) {
            System.out.println("Notification sent to " + user.getName() + ": " + message);
        }
    }

    public List<Report> viewReports() {
        return new ArrayList<>();
    }
    
    
}
