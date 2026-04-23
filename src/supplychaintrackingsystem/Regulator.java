package supplychaintrackingsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Regulator extends User {
    private int regulatorId;
    private String agencyName;
    private String accessLevel;
    private String auditRegion;
    // Multiplicity: Regulator 1 -> * Manufacturer
    private final List<Manufacturer> manufacturers = new ArrayList<>();

    public Regulator(int userID, String name, String email, String password, String role,
                     int regulatorId, String agencyName, String accessLevel, String auditRegion) {
        super(userID, name, email, password, role);
        this.regulatorId = regulatorId;
        this.agencyName = agencyName;
        this.accessLevel = accessLevel;
        this.auditRegion = auditRegion;
    }

    public void auditSupplyChainActivity() {
        System.out.println("Auditing supply chain activity in " + auditRegion);
    }

    public void viewTrackingRecords() {
        System.out.println("Viewing tracking records.");
    }

    public void reviewStorageRecord(String productID) {
        System.out.println("Reviewing storage record for product " + productID);
    }

    public boolean verifyCompliance() {
        return true;
    }

    public void generateAuditReport() {
        System.out.println("Audit report generated.");
    }

    public List<Manufacturer> getManufacturers() {
        return Collections.unmodifiableList(manufacturers);
    }

    public void addManufacturer(Manufacturer manufacturer) {
        if (manufacturer != null && !manufacturers.contains(manufacturer)) {
            manufacturers.add(manufacturer);
            manufacturer.setRegulator(this);
        }
    }
}
