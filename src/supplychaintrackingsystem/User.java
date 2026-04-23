package supplychaintrackingsystem;

public class User {
    private int userID;
    private String name;
    private String email;
    private String password;
    private String role;

    public User(int userID, String name, String email, String password, String role) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public boolean authenticate(String email, String password) {
        return this.email.equalsIgnoreCase(email) && this.password.equals(password);
    }

    public void logout() {
        // Hook for future session handling.
    }

    public User viewProfile() {
        return this;
    }

    public void updateProfile(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        if (!this.password.equals(oldPassword)) {
            return false;
        }
        this.password = newPassword;
        return true;
    }

    public int getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    protected void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{id=" + userID
                + ", name='" + name + '\''
                + ", email='" + email + '\''
                + ", role='" + role + '\''
                + '}';
    }
}
