package supplychaintrackingsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Retailer extends User implements ShipmentObserver {
    private int retailerID;
    private String storeName;
    private String contactNumber;
    private String storeLocation;
    private Inventory inventory;
    private final List<Notification> notifications = new ArrayList<>();
    private final List<Product> products = new ArrayList<>();
    private Distributor distributor;
    private final List<Shipment> shipments = new ArrayList<>();

    public Retailer(int userID, String name, String email, String password, String role) {
        super(userID, name, email, password, role);
        this.retailerID = userID;
        this.storeName = name;
        this.contactNumber = "";
        this.storeLocation = "";
    }

    public Retailer(int userID, String name, String email, String password, String role,
                    int retailerID, String storeName, String contactNumber, String storeLocation) {
        super(userID, name, email, password, role);
        this.retailerID = retailerID;
        this.storeName = storeName;
        this.contactNumber = contactNumber;
        this.storeLocation = storeLocation;
    }

    public void confirmDelivery(int shipmentID) {
        Shipment shipment = findShipment(shipmentID);
        if (shipment == null) {
            System.out.println("Shipment " + shipmentID + " was not found.");
            return;
        }

        shipment.markAsDelivered();
        shipment.setStatus("Received");
        System.out.println("Delivery confirmed for shipment " + shipmentID);
    }

    public void requestStockView() {
        if (inventory == null) {
            System.out.println("No inventory assigned to retailer " + retailerID + ".");
            return;
        }

        inventory.trackStockLevels();
        for (Product product : products) {
            product.viewProductDetails();
        }
    }

    public void requestRestock(int distributorID, int productID, int quantity) {
        System.out.println("Restock requested from distributor " + distributorID
                + " for product " + productID
                + " quantity " + quantity);
        notifications.add(new Notification(
                "Restock request sent to distributor " + distributorID + " for product " + productID));
    }

    public void requestCreateOrder(List<Product> products) {
        System.out.println("Retailer create order request for " + (products == null ? 0 : products.size()) + " products");
    }

    public void requestViewOrders() {
        System.out.println("View orders requested.");
    }

    public void receiveNotification(String message) {
        if (message != null && !message.isBlank()) {
            notifications.add(new Notification(message));
            System.out.println("Retailer notification: " + message);
        }
    }

    public int getRetailerID() {
        return retailerID;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getStoreLocation() {
        return storeLocation;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public List<Notification> getNotifications() {
        return Collections.unmodifiableList(notifications);
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public void addProduct(Product product) {
        if (product != null && !products.contains(product)) {
            products.add(product);
            product.setRetailer(this);
        }
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

    private Shipment findShipment(int shipmentID) {
        for (Shipment shipment : shipments) {
            if (shipment.getShipmentID() == shipmentID) {
                return shipment;
            }
        }
        return null;
    }

    @Override
    public void update(String message) {
        receiveNotification(message);
    }
}
