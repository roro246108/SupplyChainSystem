package supplychaintrackingsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SystemAdministrator extends User {
    // Multiplicity: SystemAdministrator 1 -> * User
    private final List<User> managedUsers = new ArrayList<>();
    // Multiplicity: SystemAdministrator 1 -> * Supplier
    private final List<Supplier> managedSuppliers = new ArrayList<>();

    public SystemAdministrator(int userID, String name, String email, String password, String role) {
        super(userID, name, email, password, role);
    }

    public void addUser(User user) {
        if (user != null && !managedUsers.contains(user)) {
            managedUsers.add(user);
        }
    }

    public void updateUser(int userID) {
        System.out.println("Updated user ID: " + userID);
    }

    public void deactivateUser(int userID) {
        System.out.println("Deactivated user ID: " + userID);
    }

    public void managePermissions(int userID, String role) {
        System.out.println("Permissions updated for user " + userID + " to role " + role);
    }

    public void monitorSystemSecurity() {
        System.out.println("Monitoring system security.");
    }

    public void reviewVerificationRequest(int supplierID) {
        System.out.println("Reviewing verification request for supplier " + supplierID);
    }

    public void receiveSystemAlert(String message) {
        System.out.println("System alert: " + message);
    }

    public void sendRequestToSupplier(int supplierID, String requestType) {
        System.out.println("Sent " + requestType + " request to supplier " + supplierID);
    }

    public void configureSecuritySettings() {
        System.out.println("Security settings configured.");
    }

    public List<User> getManagedUsers() {
        return Collections.unmodifiableList(managedUsers);
    }

    public List<Supplier> getManagedSuppliers() {
        return Collections.unmodifiableList(managedSuppliers);
    }

    public void addSupplier(Supplier supplier) {
        if (supplier != null && !managedSuppliers.contains(supplier)) {
            managedSuppliers.add(supplier);
            supplier.setAdministrator(this);
        }
    }
}
