package supplychaintrackingsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserController {
    private final List<User> users = new ArrayList<>();
    private int nextUserId = 1;

    public UserController() {
    }

    public boolean createUser(User user) {
        if (user == null) {
            System.out.println("User cannot be null.");
            return false;
        }

        ensureUserSchema();

        if (!isValidUser(user)) {
            System.out.println("Invalid user data.");
            return false;
        }

        if (isUsernameUsed(user.getUsername()) || isEmailUsed(user.getEmail())) {
            System.out.println("Username or email already exists.");
            return false;
        }

        try {
            int userId = insertUser(user.getUsername(), user.getName(), user.getEmail(), "", user.getPassword(), user.getRole());
            User storedUser = new User(userId, user.getUsername(), user.getName(), user.getEmail(), user.getPassword(), user.getRole());
            users.add(storedUser);
            nextUserId = Math.max(nextUserId, userId + 1);
            System.out.println("Create user: " + storedUser);
            return true;
        } catch (Exception ex) {
            System.out.println("Failed to create user: " + ex.getMessage());
            return false;
        }
    }

    public User registerUser(String username, String name, String email, String phone, String password, String role) {
        try {
            ensureUserSchema();
            validateRegistrationInput(username, name, email, phone, password, role);

            if (isUsernameUsed(username)) {
                System.out.println("Username already exists: " + username);
                return null;
            }

            if (isEmailUsed(email)) {
                System.out.println("Email already exists: " + email);
                return null;
            }

            int userId = insertUser(username, name, email, phone, password, role);
            User user = new User(userId, username, name, email, password, role);
            users.add(user);
            nextUserId = Math.max(nextUserId, userId + 1);
            return user;
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Failed to register user: " + ex.getMessage());
        }
        return null;
    }

    public void updateUser(int userID, String data) {
        ensureUserSchema();

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

        try {
            switch (key) {
                case "username":
                    if (isUsernameUsed(value) && !user.getUsername().equalsIgnoreCase(value)) {
                        System.out.println("Username already exists: " + value);
                        return;
                    }
                    updateUserField(userID, "username", value);
                    user.setUsername(value);
                    break;
                case "name":
                    updateUserField(userID, "full_name", value);
                    user.updateProfile(value, user.getEmail());
                    break;
                case "email":
                    if (isEmailUsed(value) && !user.getEmail().equalsIgnoreCase(value)) {
                        System.out.println("Email already exists: " + value);
                        return;
                    }
                    updateUserField(userID, "email", value);
                    user.updateProfile(user.getName(), value);
                    break;
                case "role":
                    updateUserField(userID, "role", value);
                    user.setRole(value);
                    break;
                default:
                    System.out.println("Unsupported update field: " + key);
                    return;
            }

            System.out.println("Update user " + userID + " with " + data);
        } catch (Exception ex) {
            System.out.println("Failed to update user: " + ex.getMessage());
        }
    }

    public void deleteUser(int userID) {
        ensureUserSchema();

        User user = getUser(userID);
        if (user == null) {
            System.out.println("User " + userID + " not found.");
            return;
        }

        try {
            deleteUserFromDatabase(userID);
            users.remove(user);
            System.out.println("Delete user " + userID);
        } catch (Exception ex) {
            System.out.println("Failed to delete user: " + ex.getMessage());
        }
    }

    public User getUser(int userID) {
        ensureUserSchema();

        for (User user : users) {
            if (user.getUserID() == userID) {
                return user;
            }
        }

        reloadUsers();
        for (User user : users) {
            if (user.getUserID() == userID) {
                return user;
            }
        }
        return null;
    }

    public String getUserPhone(int userID) {
        ensureUserSchema();

        try (Connection conn = DBConnection.connect()) {
            if (conn == null) {
                return "";
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT phone FROM users WHERE user_id = ?")) {
                ps.setInt(1, userID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String phone = rs.getString("phone");
                        return phone == null ? "" : phone.trim();
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("Failed to load user phone: " + ex.getMessage());
        }

        return "";
    }

    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    public User authenticateUser(String identifier, String password, String role) {
        if (identifier == null || identifier.isBlank() || password == null || password.isBlank()) {
            return null;
        }

        try {
            User user = findUser(identifier, role);
            if (user == null) {
                return null;
            }

            return user.authenticate(user.getEmail(), password) ? user : null;
        } catch (Exception ex) {
            System.out.println("Failed to authenticate user: " + ex.getMessage());
            return null;
        }
    }

    public User findUser(String identifier, String role) {
        if (identifier == null || identifier.isBlank()) {
            return null;
        }

        ensureUserSchema();

        String normalizedIdentifier = identifier.trim().toLowerCase();
        String normalizedRole = normalizeRole(role);

        try (Connection conn = DBConnection.connect()) {
            if (conn == null) {
                return null;
            }

            String sql = "SELECT user_id, username, full_name, email, phone, password, role, active "
                    + "FROM users WHERE LOWER(username) = ? OR LOWER(email) = ? OR LOWER(full_name) = ?";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, normalizedIdentifier);
                ps.setString(2, normalizedIdentifier);
                ps.setString(3, normalizedIdentifier);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        User user = mapRowToUser(rs);
                        if (normalizedRole.isBlank() || roleMatches(user.getRole(), role)) {
                            return user;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("Failed to find user: " + ex.getMessage());
        }

        return null;
    }

    private void reloadUsers() {
        ensureUserSchema();
        users.clear();

        try (Connection conn = DBConnection.connect()) {
            if (conn == null) {
                return;
            }

            String sql = "SELECT user_id, username, full_name, email, phone, password, role, active "
                    + "FROM users ORDER BY user_id";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    users.add(mapRowToUser(rs));
                }
            }

            if (!users.isEmpty()) {
                nextUserId = users.get(users.size() - 1).getUserID() + 1;
            }
        } catch (Exception ex) {
            System.out.println("Failed to reload users: " + ex.getMessage());
        }
    }

    private int insertUser(String username, String name, String email, String phone, String password, String role) throws Exception {
        String sql = "INSERT INTO users (username, full_name, email, phone, password, role, active) "
                + "VALUES (?, ?, ?, ?, ?, ?, 1)";

        try (Connection conn = DBConnection.connect()) {
            if (conn == null) {
                throw new IllegalStateException("Database connection is not available.");
            }

            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, username.trim());
                ps.setString(2, name.trim());
                ps.setString(3, email.trim().toLowerCase());
                ps.setString(4, phone.trim());
                ps.setString(5, password);
                ps.setString(6, role.trim());
                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        return keys.getInt(1);
                    }
                }
            }
        }

        throw new IllegalStateException("Could not retrieve generated user ID.");
    }

    private void updateUserField(int userID, String column, String value) throws Exception {
        String sql = "UPDATE users SET " + column + " = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.connect()) {
            if (conn == null) {
                throw new IllegalStateException("Database connection is not available.");
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, value);
                ps.setInt(2, userID);
                ps.executeUpdate();
            }
        }
    }

    private void deleteUserFromDatabase(int userID) throws Exception {
        try (Connection conn = DBConnection.connect()) {
            if (conn == null) {
                throw new IllegalStateException("Database connection is not available.");
            }

            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE user_id = ?")) {
                ps.setInt(1, userID);
                ps.executeUpdate();
            }
        }
    }

    private User mapRowToUser(ResultSet rs) throws Exception {
        int userId = rs.getInt("user_id");
        String username = rs.getString("username");
        String fullName = rs.getString("full_name");
        String email = rs.getString("email");
        String password = rs.getString("password");
        String role = rs.getString("role");
        return new User(userId, username, fullName, email, password, role);
    }

    private void validateRegistrationInput(String username, String name, String email, String phone, String password, String role) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }

        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Phone cannot be empty.");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Role cannot be empty.");
        }
    }

    private boolean isValidUser(User user) {
        return user.getUserID() > 0
                && user.getUsername() != null && !user.getUsername().isBlank()
                && user.getName() != null && !user.getName().isBlank()
                && user.getEmail() != null && !user.getEmail().isBlank()
                && user.getPassword() != null && !user.getPassword().isBlank()
                && user.getRole() != null && !user.getRole().isBlank();
    }

    private boolean isUsernameUsed(String username) {
        if (username == null || username.isBlank()) {
            return false;
        }

        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }

        try (Connection conn = DBConnection.connect()) {
            if (conn == null) {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM users WHERE LOWER(username) = ? LIMIT 1")) {
                ps.setString(1, username.trim().toLowerCase());
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (Exception ex) {
            System.out.println("Failed to check username: " + ex.getMessage());
            return false;
        }
    }

    private boolean isEmailUsed(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }

        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }

        try (Connection conn = DBConnection.connect()) {
            if (conn == null) {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM users WHERE LOWER(email) = ? LIMIT 1")) {
                ps.setString(1, email.trim().toLowerCase());
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (Exception ex) {
            System.out.println("Failed to check email: " + ex.getMessage());
            return false;
        }
    }

    private boolean roleMatches(String actualRole, String requestedRole) {
        if (requestedRole == null || requestedRole.isBlank()) {
            return true;
        }

        return normalizeRole(actualRole).equals(normalizeRole(requestedRole));
    }

    private String normalizeRole(String role) {
        if (role == null) {
            return "";
        }

        return role.replaceAll("\\s+", "").trim().toLowerCase();
    }

    private void ensureUserSchema() {
        DBConnection.ensureUsersTable();
    }
}
