package supplychaintrackingsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//rawan 
public class Distributor extends User implements ShipmentObserver {
    private String warehouseLocation;
    private final List<String> notifications = new ArrayList<>(); //rawan
    private final List<Retailer> retailers = new ArrayList<>();
    private final List<Shipment> shipments = new ArrayList<>();
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
    
    
    //rawan
    public List<String> getNotifications() {
    return Collections.unmodifiableList(notifications);
}
   
// assign to retailer rawan
   public boolean assignProductToRetailer(int productID, int retailerID) {

    if (productID <= 0 || retailerID <= 0) {
        return false;
    }

    System.out.println("Product " + productID + " assigned to retailer " + retailerID);

    return true;
}
   
   //getter rawan
    public String getWarehouseLocation() {
        return warehouseLocation;
    }

    //getters rawan
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

   

    public void addLogistics(Logistics logisticsPerson) {
        if (logisticsPerson != null && !logistics.contains(logisticsPerson)) {
            logistics.add(logisticsPerson);
            logisticsPerson.setDistributor(this);
        }
    }
    
    
    // main function on the observer 
   public void addShipment(Shipment shipment) {

    if (shipment == null) {
        throw new IllegalArgumentException("Shipment is null");
    }

    if (!shipments.contains(shipment)) {

        shipments.add(shipment);

        if (shipment.getDistributor() != this) {
            shipment.setDistributor(this);
        }

        shipment.registerObserver(this);
    }
}
   
   
   // remove shipment rawan
  public void removeShipment(Shipment shipment) {

    if (shipment == null) return;

    if (shipments.remove(shipment)) {

        if (shipment.getDistributor() == this) {
            shipment.setDistributor(null);
        }

        shipment.removeObserver(this);
    }
}
   
    
    // schedule delivery rawan 
    public void scheduleDelivery(Shipment shipment) {

    if (shipment == null) {
        throw new IllegalArgumentException("Shipment is null");
    }

    System.out.println("Delivery scheduled for shipment " + shipment.getShipmentID());
}
    
    //recieve notfications rawan
   public void receiveNotification(String message) {

    if (message == null || message.trim().isEmpty()) {
        System.out.println("Invalid notification");
        return;
    }

    notifications.add(message);

    
    if (message.contains("DELIVERED")) {
        System.out.println("Distributor: shipment delivered, updating records...");
        updateDistributionRecords();
    }

    System.out.println("Distributor received: " + message);
}
    //rawan
  @Override
  public void update(String msg) {
    receiveNotification(msg);
  }
    
}
