package supplychaintrackingsystem;

public class UserController {
    public void createUser(User user) {
        System.out.println("Create user: " + user);
    }

    public void updateUser(int userID, String data) {
        System.out.println("Update user " + userID + " with " + data);
    }

    public void deleteUser(int userID) {
        System.out.println("Delete user " + userID);
    }

    public User getUser(int userID) {
        return null;
    }
}
