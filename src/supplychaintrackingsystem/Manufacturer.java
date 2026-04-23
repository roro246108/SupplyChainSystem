package supplychaintrackingsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Manufacturer extends User {
    private String facilityLocation;
    private Regulator regulator;
    // Multiplicity: Manufacturer 1 -> * Retailer
    private final List<Retailer> retailers = new ArrayList<>();
    // Multiplicity: Manufacturer 1 -> * Shipment
    private final List<Shipment> shipments = new ArrayList<>();

    public Manufacturer(int userID, String name, String email, String password, String role,
                         String facilityLocation) {
        super(userID, name, email, password, role);
        this.facilityLocation = facilityLocation;
    }

    public void monitorStorageConditions() {
        System.out.println("Monitoring storage conditions at " + facilityLocation);
    }

    public void requestMaintenance() {
        System.out.println("Maintenance requested.");
    }

    public void startProduction(Product product) {
        System.out.println("Starting production for " + (product == null ? "unknown" : product.getProductName()));
    }

    public List<ProductionRecord> viewProductionRecords() {
        return Collections.unmodifiableList(new ArrayList<>());
    }

    public List<Retailer> getRetailers() {
        return Collections.unmodifiableList(retailers);
    }

    public List<Shipment> getShipments() {
        return Collections.unmodifiableList(shipments);
    }

    public void addRetailer(Retailer retailer) {
        if (retailer != null && !retailers.contains(retailer)) {
            retailers.add(retailer);
        }
    }

    public void addShipment(Shipment shipment) {
        if (shipment != null && !shipments.contains(shipment)) {
            shipments.add(shipment);
            shipment.setManufacturer(this);
        }
    }

    public Regulator getRegulator() {
        return regulator;
    }

    public void setRegulator(Regulator regulator) {
        this.regulator = regulator;
    }
}
