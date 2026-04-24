package supplychaintrackingsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Supplier extends User implements ShipmentObserver {
    private int contactNumber;
    
    private boolean verified = false; //Fatma
private final List<String> receivedRequests = new ArrayList<>();  //fatma

    private SystemAdministrator administrator;
    // Multiplicity: Supplier 1 -> * Shipment
    private final List<Shipment> shipments = new ArrayList<>();
    // Multiplicity: Supplier 1 -> * Notification
    private final List<Notification> notifications = new ArrayList<>();

    public Supplier(int userID, String name, String email, String password, String role, int contactNumber) {
        super(userID, name, email, password, role);
        this.contactNumber = contactNumber;
    }
    
    //////////////////////////////////////////////////
    //Fatma
    
        // Needed by SystemAdministrator
    public int getSupplierID() {
        return getUserID();
    }

    // Needed by SystemAdministrator
    public boolean isVerified() {
        return verified;
    }

    // Needed by SystemAdministrator
    public void setVerified(boolean verified) {
        this.verified = verified;

        if (verified) {
            receiveNotification("Your supplier account has been verified.");
        } else {
            receiveNotification("Your supplier account verification has been removed.");
        }
    }

    // Needed by SystemAdministrator
    public void receiveRequest(String request) {
        if (request == null || request.isBlank()) {
            throw new IllegalArgumentException("Admin request cannot be empty.");
        }

        receivedRequests.add(request);
        receiveNotification("New admin request received: " + request);
    }
    
    ///////////////////////////////////////////////////////
    
    

    public Report generateSupplyReport() {
        return new Report("Supply Report for " + getName());
    }

    public void supplyRawMaterial(RawMaterial material) {
        if (material != null) {
            System.out.println("Supplying raw material: " + material.getMaterialID());
        }
    }

    public List<Notification> viewNotifications() {
        return Collections.unmodifiableList(notifications);
    }

    public void requestAccountVerification() {
        System.out.println("Supplier verification requested.");
    }

    public void receiveNotification(String message) {
        notifications.add(new Notification(message));
    }

    public boolean respondToAdminRequest(int requestID) {
        return requestID > 0;
    }

    public SystemAdministrator getAdministrator() {
        return administrator;
    }

    public void setAdministrator(SystemAdministrator administrator) {
        this.administrator = administrator;
    }

    public List<Shipment> getShipments() {
        return Collections.unmodifiableList(shipments);
    }

    public void addShipment(Shipment shipment) {
        if (shipment == null || shipments.contains(shipment)) {
            return;
        }
        shipments.add(shipment);
        shipment.setSupplier(this);
    }

    public void addNotification(Notification notification) {
        if (notification != null) {
            notifications.add(notification);
        }
    }

    @Override
    public void update(String message) {
        receiveNotification(message);
    }
}
