package supplychaintrackingsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Supplier extends User implements ShipmentObserver {
   
    
   
private final List<String> receivedRequests = new ArrayList<>();  //fatma

    private int supplierID;
    private String companyName;
    private String contactNumber;
    private String address;
    private String verificationStatus;
    private SystemAdministrator administrator;
    private final List<Product> suppliedProducts = new ArrayList<>();
    // Multiplicity: Supplier 1 -> * Shipment
    private final List<Shipment> shipments = new ArrayList<>();
    // Multiplicity: Supplier 1 -> * Notification
    private final List<Notification> notifications = new ArrayList<>();

    public Supplier(int userID, String name, String email, String password, String role,
                    int supplierID, String companyName, String contactNumber, String address) {
        super(userID, name, email, password, role);
        this.supplierID = supplierID;
        this.companyName = companyName;
        this.contactNumber = contactNumber;
        this.address = address;
        this.verificationStatus = "Pending";
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
        String reportText = "Supplier Report for " + companyName
                + " | supplierID=" + supplierID
                + " | products=" + suppliedProducts.size()
                + " | shipments=" + shipments.size()
                + " | notifications=" + notifications.size()
                + " | verificationStatus=" + verificationStatus
                + " | generatedAt=" + new Date();
        return new Report(reportText);
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
        verificationStatus = "Verification Requested";
        notifications.add(new Notification("Account verification requested for " + companyName));
        if (administrator != null) {
            administrator.sendNotification("Supplier " + companyName + " requested account verification.");
        }
    }

    public void receiveNotification(String message) {
        if (message != null && !message.isBlank()) {
            notifications.add(new Notification(message));
        }
    }

    public boolean respondToAdminRequest(int requestID) {
        if (requestID <= 0) {
            return false;
        }
        notifications.add(new Notification("Responded to admin request " + requestID));
        return true;
    }

    public SystemAdministrator getAdministrator() {
        return administrator;
    }

    public void setAdministrator(SystemAdministrator administrator) {
        this.administrator = administrator;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public List<Product> getSuppliedProducts() {
        return Collections.unmodifiableList(suppliedProducts);
    }

    public void addSuppliedProduct(Product product) {
        if (product != null && !suppliedProducts.contains(product)) {
            suppliedProducts.add(product);
        }
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
