package supplychaintrackingsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Customer extends User implements ShipmentObserver {
    private String address;
   private List<String> notifications = new ArrayList<>();
   private List<String> supportRequests = new ArrayList<>();
    private final List<Shipment> shipments = new ArrayList<>();

    public Customer(int userID, String name, String email, String password, String role, String address) {
        super(userID, name, email, password, role);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public List<Shipment> getShipments() {
        return shipments;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    //change delivery method rawan
   public boolean changeDeliveryAddress(String newAddress) {

    // check null or empty
    if (newAddress == null || newAddress.trim().isEmpty()) {
        System.out.println("Invalid address: empty value");
        return false;
    }

    // check if same as current
    if (newAddress.equalsIgnoreCase(this.address)) {
        System.out.println("Address is the same as current one");
        return false;
    }

    // simple length validation
    if (newAddress.length() < 10) {
        System.out.println("Address is too short");
        return false;
    }

    // update address
    String oldAddress = this.address;
    this.address = newAddress;

    // simulate system behavior (log / notify)
    System.out.println("Address updated from [" + oldAddress + "] to [" + newAddress + "]");

    return true;
}
     //recieve notification method rawan
    public void receiveNotification(String message) {
        try {
            // validation
            if (message == null || message.trim().isEmpty()) {
                throw new IllegalArgumentException("Notification message is invalid");
            }

            // save notification
            notifications.add(message);

            // simulate delivery
            System.out.println("New notification for " + getName() + ": " + message);

        } catch (IllegalArgumentException e) {
            System.out.println("Failed to receive notification: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error while receiving notification");
        }
    }

    public List<String> getNotifications() {
        return notifications;
    }
   

     //contact support method  rawan
     public void contactSupport(String message) {
        try {
            // validation
            if (message == null || message.trim().isEmpty()) {
                throw new IllegalArgumentException("Support message is invalid");
            }

            // create request with simple ID (timestamp)
            String request = "REQ-" + System.currentTimeMillis() + ": " + message;

            // store request
            supportRequests.add(request);

            // simulate sending
            System.out.println("Support request sent successfully: " + request);

        } catch (IllegalArgumentException e) {
            System.out.println("Failed to send support request: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error while contacting support");
        }
    }

    public List<String> getSupportRequests() {
        return supportRequests;
    }
    
    // reguest create order method rawan
public Order requestCreateOrder(int orderID, List<Product> products) {

    if (products == null || products.isEmpty()) {
        throw new IllegalArgumentException("Products list is empty");
    }

    Order order = new Order(orderID, this, products);

    System.out.println("Customer " + getName() + " created order " + orderID);

    return order;
}
    
  // reguest track order method rawan
    public String requestTrackOrder(Order order) {

    if (order == null) {
        throw new IllegalArgumentException("Order is null");
    }

    return order.trackOrder();
}

    public List<Shipment> viewDeliveryUpdates() {
        return Collections.unmodifiableList(shipments);
    }

    
      // add shipment method rawan
   public void addShipment(Shipment shipment) {

    if (shipment == null) return;

    if (!shipments.contains(shipment)) {
        shipments.add(shipment);

        if (shipment.getCustomer() != this) {
            shipment.setCustomer(this);
        }
    }
}
// remove shipement method rawan 
   public void removeShipment(Shipment shipment) {

    if (shipment == null) return;

    if (shipments.remove(shipment)) {

        if (shipment.getCustomer() == this) {
            shipment.setCustomer(null);
        }
    }
}
    //obserever override method rawan  

    @Override
    public void update(String message) {
        receiveNotification(message);
    }
}
