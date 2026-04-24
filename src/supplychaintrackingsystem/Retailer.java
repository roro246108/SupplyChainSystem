package supplychaintrackingsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Retailer extends User implements ShipmentObserver {
    // Multiplicity: Retailer 1 -> 1 Distributor
    private Distributor distributor;
    // Multiplicity: Retailer 1 -> * Shipment
    private final List<Shipment> shipments = new ArrayList<>();

    public Retailer(int userID, String name, String email, String password, String role) {
        super(userID, name, email, password, role);
    }

    public void confirmDelivery(int shipment) {
        System.out.println("Delivery confirmed for shipment " + shipment);
    }

    public void requestStockView() {
        System.out.println("Stock view requested.");
    }

    public void requestRestock(int distributorID, String productID, int quantity) {
        System.out.println("Restock requested from distributor " + distributorID);
    }

    public void requestCreateOrder(List<Product> products) {
        System.out.println("Retailer create order request for " + (products == null ? 0 : products.size()) + " products");
    }

    public void requestViewOrders() {
        System.out.println("View orders requested.");
    }

    public void receiveNotification(String message) {
        System.out.println("Retailer notification: " + message);
    }

    public Distributor getDistributor() {
        return distributor;
    }

    public void setDistributor(Distributor distributor) {
        this.distributor = distributor;
        if (distributor != null) {
            distributor.addRetailer(this);
        }
    }

    public List<Shipment> getShipments() {
        return Collections.unmodifiableList(shipments);
    }

    public void addShipment(Shipment shipment) {
        if (shipment != null && !shipments.contains(shipment)) {
            shipments.add(shipment);
            shipment.setRetailer(this);
        }
    }

    @Override
    public void update(String message) {
        receiveNotification(message);
    }
}
