package supplychaintrackingsystem;

public class AuthBoundary {
    private final UserController controller;

    public AuthBoundary(UserController controller) {
        this.controller = controller;
    }

    public User register(String username, String name, String email, String phone, String password, String role) {
        return controller.registerUser(username, name, email, phone, password, role);
    }

    public User login(String identifier, String password, String role) {
        return controller.authenticateUser(identifier, password, role);
    }

    public UserController getController() {
        return controller;
    }
}
