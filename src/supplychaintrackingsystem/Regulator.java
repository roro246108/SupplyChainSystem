package supplychaintrackingsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Regulator extends User {
    private int regulatorId;
    private String agencyName;
    private String accessLevel;
    private String auditRegion;
    private final List<Manufacturer> manufacturers = new ArrayList<>();

    public Regulator(int regulatorId, String agencyName, String accessLevel, String auditRegion, int userID, String name, String email, String password, String role) {
        super(userID, name, email, password, role);
        this.regulatorId = regulatorId;
        this.agencyName = agencyName;
        this.accessLevel = accessLevel;
        this.auditRegion = auditRegion;
        
    }

    public int getRegulatorId() {
        return regulatorId;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public String getAuditRegion() {
        return auditRegion;
    }

    // audit rawan
    
    public void auditSupplyChainActivity() {

        if (manufacturers.isEmpty()) {
            System.out.println("No manufacturers assigned for audit.");
            return;
        }

        System.out.println(
            "Audit started by " + agencyName +
            " in region " + auditRegion +
            " for " + manufacturers.size() + " manufacturer(s)."
        );
    }
    
    //view rawan
    
   public void viewTrackingRecords(Order order) {

        if (order == null) {
            System.out.println("Order not found.");
            return;
        }

        System.out.println("Order ID: " + order.getOrderID());
        System.out.println("Status: " + order.getOrderStatus());
        System.out.println("Tracking Number: " + order.getTrackingNumber());
    }
   
   // review rawan
   
    public void reviewStorageRecord(String productID) {

        if (productID == null || productID.trim().isEmpty()) {
            System.out.println("Invalid product ID.");
            return;
        }

        System.out.println(
            "Storage record reviewed for product " + productID +
            " in region " + auditRegion
        );
    }

    // generate rawan

   public Report generateAuditReport() {

        String details =
            "Audit Report | Agency: " + agencyName +
            " | Region: " + auditRegion +
            " | Manufacturers: " + manufacturers.size();

        return new Report(details);
    }

    public List<Manufacturer> getManufacturers() {
        return Collections.unmodifiableList(manufacturers);
    }
    
    // add manufacture rawan 
public void addManufacturer(Manufacturer manufacturer) {

        if (manufacturer == null) {
            throw new IllegalArgumentException("Manufacturer is null");
        }

        if (!manufacturers.contains(manufacturer)) {
            manufacturers.add(manufacturer);
            manufacturer.setRegulator(this);
        }
    }
// remove manfacture rawan
public void removeManufacturer(Manufacturer manufacturer) {

        if (manufacturer == null) {
            return;
        }

        if (manufacturers.remove(manufacturer)) {
            manufacturer.setRegulator(null);
        }
    }




}