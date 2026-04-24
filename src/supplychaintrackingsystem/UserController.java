package supplychaintrackingsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserController {
    private final List<User> users = new ArrayList<>();

    public void createUser(User user) {
        if (user == null) {
            System.out.println("User cannot be null.");
            return;
        }
        if (!isValidUser(user)) {
            System.out.println("Invalid user data.");
            return;
        }
        if (isEmailUsed(user.getEmail())) {
            System.out.println("Email already exists: " + user.getEmail());
            return;
        }

        users.add(user);
        System.out.println("Create user: " + user);
    }

    public void updateUser(int userID, String data) {
        User user = getUser(userID);
        if (user == null) {
            System.out.println("User " + userID + " not found.");
            return;
        }

        if (data == null || data.isBlank()) {
            System.out.println("No update data provided.");
            return;
        }

        String[] parts = data.split("=");
        if (parts.length != 2) {
            System.out.println("Update data must be in key=value format.");
            return;
        }

        String key = parts[0].trim().toLowerCase();
        String value = parts[1].trim();

        switch (key) {
            case "name":
                user.updateProfile(value, user.getEmail());
                break;
            case "email":
                if (isEmailUsed(value) && !user.getEmail().equalsIgnoreCase(value)) {
                    System.out.println("Email already exists: " + value);
                    return;
                }
                user.updateProfile(user.getName(), value);
                break;
            case "role":
                user.setRole(value);
                break;
            default:
                System.out.println("Unsupported update field: " + key);
                return;
        }

        System.out.println("Update user " + userID + " with " + data);
    }

    public void deleteUser(int userID) {
        User user = getUser(userID);
        if (user == null) {
            System.out.println("User " + userID + " not found.");
            return;
        }

        users.remove(user);
        System.out.println("Delete user " + userID);
    }

    public User getUser(int userID) {
        for (User user : users) {
            if (user.getUserID() == userID) {
                return user;
            }
        }
        return null;
    }

    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    private boolean isValidUser(User user) {
        return user.getUserID() > 0
                && user.getName() != null && !user.getName().isBlank()
                && user.getEmail() != null && !user.getEmail().isBlank()
                && user.getRole() != null && !user.getRole().isBlank();
    }

    private boolean isEmailUsed(String email) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }
}
