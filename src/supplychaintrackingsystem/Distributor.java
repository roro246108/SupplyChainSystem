package supplychaintrackingsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Distributor extends User {
    private String warehouseLocation;
    // Multiplicity: Distributor 1 -> * Retailer
    private final List<Retailer> retailers = new ArrayList<>();
    // Multiplicity: Distributor 1 -> * Shipment
    private final List<Shipment> shipments = new ArrayList<>();
    // Multiplicity: Distributor 1 -> * Logistics
    private final List<Logistics> logistics = new ArrayList<>();

    public Distributor(int userID, String name, String email, String password, String role,
                       String warehouseLocation) {
        super(userID, name, email, password, role);
        this.warehouseLocation = warehouseLocation;
    }

    public String organizeDistributionPlan() {
        return "Distribution plan for " + warehouseLocation;
    }

    public Report generateDistributionReport() {
        return new Report("Distribution Report");
    }

    public void updateDistributionRecords() {
        System.out.println("Distribution records updated.");
    }

    public void update(String msg) {
        System.out.println("Distributor update: " + msg);
    }

    public boolean assignProductToRetailer(int productID, int retailerID) {
        return productID > 0 && retailerID > 0;
    }

    public void scheduleDelivery() {
        System.out.println("Delivery scheduled.");
    }

    public List<Retailer> getRetailers() {
        return Collections.unmodifiableList(retailers);
    }

    public List<Shipment> getShipments() {
        return Collections.unmodifiableList(shipments);
    }

    public List<Logistics> getLogistics() {
        return Collections.unmodifiableList(logistics);
    }

    public void addRetailer(Retailer retailer) {
        if (retailer != null && !retailers.contains(retailer)) {
            retailers.add(retailer);
            if (retailer.getDistributor() != this) {
                retailer.setDistributor(this);
            }
        }
    }

    public void addShipment(Shipment shipment) {
        if (shipment != null && !shipments.contains(shipment)) {
            shipments.add(shipment);
            shipment.setDistributor(this);
        }
    }

    public void addLogistics(Logistics logisticsPerson) {
        if (logisticsPerson != null && !logistics.contains(logisticsPerson)) {
            logistics.add(logisticsPerson);
            logisticsPerson.setDistributor(this);
        }
    }
}
