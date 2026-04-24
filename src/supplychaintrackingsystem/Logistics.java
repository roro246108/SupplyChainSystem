package supplychaintrackingsystem;

import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Logistics extends User {
    private int logisticsID;
    private String department;
    private String assignedRegion;
    private String contactNumber;
    // Multiplicity: Logistics 1 -> * Shipment
    private final List<Shipment> shipments = new ArrayList<>();
    // Multiplicity: Logistics 1 -> 1 Distributor
    private Distributor distributor;

    public Logistics(int userID, String name, String email, String password, String role,
                     int logisticsID, String department, String assignedRegion, String contactNumber) {
        super(userID, name, email, password, role);
        this.logisticsID = logisticsID;
        this.department = department;
        this.assignedRegion = assignedRegion;
        this.contactNumber = contactNumber;
    }

    public void scheduleDelivery(int shipmentID, Date deliveryDate) {
        System.out.println("Delivery scheduled for shipment " + shipmentID + " on " + deliveryDate);
    }

    public void monitorTransportConditions(int shipmentID) {
        System.out.println("Monitoring transport conditions for shipment " + shipmentID);
    }

    public void recordDeliveryDetails(int shipmentID) {
        System.out.println("Delivery details recorded for shipment " + shipmentID);
    }

    public void reportDeliveryIssue(int shipmentID, String issue) {
        System.out.println("Shipment " + shipmentID + " issue: " + issue);
    }

    public List<Shipment> getShipments() {
        return Collections.unmodifiableList(shipments);
    }

    public Distributor getDistributor() {
        return distributor;
    }

    public void setDistributor(Distributor distributor) {
        this.distributor = distributor;
        if (distributor != null && !distributor.getLogistics().contains(this)) {
            distributor.addLogistics(this);
        }
    }

    public void addShipment(Shipment shipment) {
        if (shipment != null && !shipments.contains(shipment)) {
            shipments.add(shipment);
            shipment.setLogistics(this);
        }
    }
}
