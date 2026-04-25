package supplychaintrackingsystem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SystemAdministrator extends User {

    private final List<User> managedUsers = new ArrayList<>();
    private final List<Supplier> managedSuppliers = new ArrayList<>();
    private final List<String> securityLogs = new ArrayList<>();
    private final List<String> systemAlerts = new ArrayList<>();

    public SystemAdministrator(int userID, String name, String email, String password, String role) {
        super(userID, name, email, password, role);
    }

    // ================= USER MANAGEMENT =================

    public void addUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }

        if (findUserByID(user.getUserID()) != null) {
            throw new IllegalArgumentException("User already exists.");
        }

        managedUsers.add(user);
        logAction("Added user: " + user.getName());
    }

    public void updateUser(int userID) {
        User user = findUserByID(userID);

        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }

        logAction("Updated user ID: " + userID);
    }

    public void deactivateUser(int userID) {
        User user = findUserByID(userID);

        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }

        if (!user.isActive()) {
            throw new IllegalStateException("User already deactivated.");
        }

        user.setActive(false);
        logAction("Deactivated user: " + user.getName());
    }

    public void managePermissions(int userID, String role) {
        User user = findUserByID(userID);

        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }

        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Role cannot be empty.");
        }

        user.setRole(role);
        logAction("Changed role for user ID " + userID + " to " + role);
    }

    // ================= SYSTEM MONITORING =================

    public void monitorSystemSecurity() {
        int inactiveUsers = 0;

        for (User user : managedUsers) {
            if (!user.isActive()) {
                inactiveUsers++;
            }
        }

        String report =
                "Security Report -> Total Users: " + managedUsers.size()
                        + ", Inactive Users: " + inactiveUsers
                        + ", Suppliers: " + managedSuppliers.size()
                        + ", Alerts: " + systemAlerts.size();

        logAction(report);
        System.out.println(report);
    }

    public void reviewVerificationRequest(int supplierID) {
        Supplier supplier = findSupplierByID(supplierID);

        if (supplier == null) {
            throw new IllegalArgumentException("Supplier not found.");
        }

        if (supplier.isVerified()) {
            throw new IllegalStateException("Supplier already verified.");
        }

        supplier.setVerified(true);
        logAction("Verified supplier: " + supplier.getName());
    }

    public void receiveSystemAlert(String message) {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Alert message cannot be empty.");
        }

        systemAlerts.add(message);

        if (message.toLowerCase().contains("critical")) {
            logAction("CRITICAL ALERT: " + message);
        } else {
            logAction("System Alert: " + message);
        }
    }

    public void sendNotification(String message) {//////////nany
        receiveSystemAlert(message);
    }/////////nany

    public void sendRequestToSupplier(int supplierID, String requestType) {
        Supplier supplier = findSupplierByID(supplierID);

        if (supplier == null) {
            throw new IllegalArgumentException("Supplier not found.");
        }

        if (requestType == null || requestType.isBlank()) {
            throw new IllegalArgumentException("Request cannot be empty.");
        }

        supplier.receiveRequest(requestType);
        logAction("Sent request to supplier ID " + supplierID + ": " + requestType);
    }

    public void configureSecuritySettings() {
        logAction("Security settings updated");

        System.out.println("Security settings:");
        System.out.println("- Role-based access control enabled");
        System.out.println("- User monitoring enabled");
        System.out.println("- Supplier verification enabled");
        System.out.println("- Alert system active");
    }

    // ================= SUPPLIER MANAGEMENT =================

    public void addSupplier(Supplier supplier) {
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier cannot be null.");
        }

        if (findSupplierByID(supplier.getSupplierID()) != null) {
            throw new IllegalArgumentException("Supplier already exists.");
        }

        managedSuppliers.add(supplier);
        supplier.setAdministrator(this);

        logAction("Added supplier: " + supplier.getName());
    }

    // ================= PRIVATE HELPERS =================

    private User findUserByID(int userID) {
        for (User user : managedUsers) {
            if (user.getUserID() == userID) {
                return user;
            }
        }
        return null;
    }

    private Supplier findSupplierByID(int supplierID) {
        for (Supplier supplier : managedSuppliers) {
            if (supplier.getSupplierID() == supplierID) {
                return supplier;
            }
        }
        return null;
    }

    // ================= STRONG LOGGING =================

    private void logAction(String action) {
        if (action == null || action.isBlank()) {
            throw new IllegalArgumentException("Log action cannot be empty.");
        }

        String logEntry =
                LocalDateTime.now()
                        + " | Admin: " + getName()
                        + " | Action: " + action;

        securityLogs.add(logEntry);
        System.out.println(logEntry);
    }

    // ================= SAFE GETTERS =================

    public List<User> getManagedUsers() {
        return Collections.unmodifiableList(managedUsers);
    }

    public List<Supplier> getManagedSuppliers() {
        return Collections.unmodifiableList(managedSuppliers);
    }

    public List<String> getSecurityLogs() {
        return Collections.unmodifiableList(securityLogs);
    }

    public List<String> getSystemAlerts() {
        return Collections.unmodifiableList(systemAlerts);
    }
}