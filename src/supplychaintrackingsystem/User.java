package supplychaintrackingsystem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User {

    private int userID;
    private String name;
    private String email;
    private String password;
    private String role;
    private boolean active;
    private boolean loggedIn;

    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
    private LocalDateTime lastLogoutAt;

    private final List<String> activityLog = new ArrayList<>();

    public User(int userID, String name, String email, String password, String role) {
        validateUserID(userID);
        validateName(name);
        validateEmail(email);
        validatePassword(password);
        validateRole(role);

        this.userID = userID;
        this.name = name.trim();
        this.email = email.trim().toLowerCase();
        this.password = password;
        this.role = role.trim();
        this.active = true;
        this.loggedIn = false;
        this.createdAt = LocalDateTime.now();

        addLog("Account created for user: " + this.name);
    }

    public boolean authenticate(String email, String password) {
        if (!active) {
            throw new IllegalStateException("Login failed. This account is deactivated.");
        }

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            addLog("Failed login attempt because email or password was empty.");
            return false;
        }

        boolean validLogin =
                this.email.equalsIgnoreCase(email.trim())
                        && this.password.equals(password);

        if (validLogin) {
            loggedIn = true;
            lastLoginAt = LocalDateTime.now();
            addLog("User logged in successfully.");
            return true;
        }

        addLog("Failed login attempt using email: " + email);
        return false;
    }

    public void logout() {
        if (!loggedIn) {
            throw new IllegalStateException("User is not currently logged in.");
        }

        loggedIn = false;
        lastLogoutAt = LocalDateTime.now();
        addLog("User logged out successfully.");
    }

    public User viewProfile() {
        if (!active) {
            throw new IllegalStateException("Cannot view profile. Account is deactivated.");
        }

        addLog("Profile viewed.");
        return this;
    }

    public void updateProfile(String newName, String newEmail) {
        if (!active) {
            throw new IllegalStateException("Cannot update profile. Account is deactivated.");
        }

        boolean updated = false;

        if (newName != null && !newName.isBlank()) {
            validateName(newName);
            this.name = newName.trim();
            updated = true;
            addLog("Name updated.");
        }

        if (newEmail != null && !newEmail.isBlank()) {
            validateEmail(newEmail);
            this.email = newEmail.trim().toLowerCase();
            updated = true;
            addLog("Email updated.");
        }

        if (!updated) {
            throw new IllegalArgumentException("No valid profile information was provided.");
        }
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        if (!active) {
            throw new IllegalStateException("Cannot change password. Account is deactivated.");
        }

        if (oldPassword == null || !this.password.equals(oldPassword)) {
            addLog("Failed password change attempt.");
            return false;
        }

        validatePassword(newPassword);

        if (newPassword.equals(oldPassword)) {
            throw new IllegalArgumentException("New password cannot be the same as the old password.");
        }

        this.password = newPassword;
        addLog("Password changed successfully.");
        return true;
    }

    public void deactivateAccount() {
        if (!active) {
            throw new IllegalStateException("Account is already deactivated.");
        }

        active = false;
        loggedIn = false;
        addLog("Account deactivated.");
    }

    public void reactivateAccount() {
        if (active) {
            throw new IllegalStateException("Account is already active.");
        }

        active = true;
        addLog("Account reactivated.");
    }

    public int getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        validateName(name);
        this.name = name.trim();
        addLog("Name changed by system/admin.");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        validateEmail(email);
        this.email = email.trim().toLowerCase();
        addLog("Email changed by system/admin.");
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        validateRole(role);
        this.role = role.trim();
        addLog("Role changed to: " + this.role);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        if (this.active == active) {
            return;
        }

        this.active = active;

        if (!active) {
            loggedIn = false;
            addLog("Account deactivated by system/admin.");
        } else {
            addLog("Account activated by system/admin.");
        }
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public LocalDateTime getLastLogoutAt() {
        return lastLogoutAt;
    }

    public List<String> getActivityLog() {
        return Collections.unmodifiableList(activityLog);
    }

    private void validateUserID(int userID) {
        if (userID <= 0) {
            throw new IllegalArgumentException("User ID must be positive.");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }

        if (name.trim().length() < 2) {
            throw new IllegalArgumentException("Name must be at least 2 characters.");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }

        boolean hasLetter = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            }

            if (Character.isDigit(c)) {
                hasDigit = true;
            }
        }

        if (!hasLetter || !hasDigit) {
            throw new IllegalArgumentException("Password must contain letters and numbers.");
        }
    }

    private void validateRole(String role) {
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Role cannot be empty.");
        }

        List<String> validRoles = List.of(
                "Customer",
                "Supplier",
                "Retailer",
                "Manufacturer",
                "Logistics",
                "Administrator"
        );

        if (!validRoles.contains(role.trim())) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    private void addLog(String message) {
        activityLog.add(LocalDateTime.now() + " - " + message);
    }

    @Override
    public String toString() {
        return "User{"
                + "userID=" + userID
                + ", name='" + name + '\''
                + ", email='" + email + '\''
                + ", role='" + role + '\''
                + ", active=" + active
                + ", loggedIn=" + loggedIn
                + '}';
    }
}