package supplychaintrackingsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Customer extends User implements ShipmentObserver {
    private String address;
    // Multiplicity: Customer 1 -> * Shipment
    private final List<Shipment> shipments = new ArrayList<>();

    public Customer(int userID, String name, String email, String password, String role, String address) {
        super(userID, name, email, password, role);
        this.address = address;
    }

    public void changeDeliveryAddress(String newAddress) {
        this.address = newAddress;
    }

    public void receiveNotification(String message) {
        System.out.println("Customer notification: " + message);
    }

    public void contactSupport(String message) {
        System.out.println("Support request: " + message);
    }

    public void requestCreateOrder(List<Product> products) {
        System.out.println("Request create order for " + (products == null ? 0 : products.size()) + " products");
    }

    public void requestTrackOrder(int orderID) {
        System.out.println("Track order " + orderID);
    }

    public List<Shipment> viewDeliveryUpdates() {
        return Collections.unmodifiableList(shipments);
    }

    public void addShipment(Shipment shipment) {
        if (shipment == null || shipments.contains(shipment)) {
            return;
        }
        shipments.add(shipment);
        shipment.setCustomer(this);
    }

    public void removeShipment(Shipment shipment) {
        if (shipments.remove(shipment) && shipment != null) {
            shipment.setCustomer(null);
        }
    }

    @Override
    public void update(String message) {
        receiveNotification(message);
    }
}
