package supplychaintrackingsystem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Shipment implements ShipmentSubject {
    private int shipmentID;
    private String origin;
    private String destination;
    private String currentLocation;
    private double weight;
    private double distance;
    private double temperature;
    private double humidity;
    private Date departureDate;
    private Date arrivalDate;
    private ShipmentStatus status;//rawan
    private ShippingStrategy shippingStrategy;
    private final List<ShipmentObserver> observers = new ArrayList<>();
    private Order order;
    private Customer customer;
    private Supplier supplier;
    private Distributor distributor;
    private Logistics logistics;
    private Retailer retailer;
    private Manufacturer manufacturer;

    
    //rawan
    public Shipment(int shipmentID, String origin, String destination, String currentLocation, double weight, double distance, double temperature, double humidity, Date departureDate, Date arrivalDate, ShipmentStatus status, ShippingStrategy shippingStrategy, Order order, Customer customer, Supplier supplier, Distributor distributor, Logistics logistics, Retailer retailer, Manufacturer manufacturer) {
        this.shipmentID = shipmentID;
        this.origin = origin;
        this.destination = destination;
        this.currentLocation = currentLocation;
        this.weight = weight;
        this.distance = distance;
        this.temperature = temperature;
        this.humidity = humidity;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.status = status;
        this.shippingStrategy = shippingStrategy;
        this.order = order;
        this.customer = customer;
        this.supplier = supplier;
        this.distributor = distributor;
        this.logistics = logistics;
        this.retailer = retailer;
        this.manufacturer = manufacturer;
    }

 
    public Shipment(int shipmentID) {
        this.shipmentID = shipmentID;
    }

    public Shipment(Order order) {
        this.order = order;
    }

    public int getShipmentID() {
        return shipmentID;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public double getWeight() {
        return weight;
    }

    public double getDistance() {
        return distance;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public ShipmentStatus getStatus() {
        return status;
    }

   

    public void setStrategy(ShippingStrategy strategy) {
        this.shippingStrategy = strategy;
    }

    // set order rawan
    public void setOrder(Order order) {
    if (order == null) {
        throw new IllegalArgumentException("Order is null");
    }
    this.order = order;
}

    
    // main function of observer rawan
    public void setCustomer(Customer customer) {
    this.customer = customer;

    if (customer != null) {
        registerObserver(customer); //from here start the pattern
    }
}
    
    
    public void sendShipmentToRetailer(int shipmentID, int retailerID) {
        this.shipmentID = shipmentID;
        System.out.println("Send shipment " + shipmentID + " to retailer " + retailerID);
    }

    public Shipment createShipment(Shipment shipment) {
        return shipment;
    }

    public Shipment createShipment(Order order) {
        this.order = order;
        return this;
    }

    public void receiveShipment(int shipmentID) {
        this.shipmentID = shipmentID;
    }

    public void updateShipmentStatus(int shipmentID, ShipmentStatus  status) {
        this.shipmentID = shipmentID;
        this.status  = status ;
        notifyObservers("Shipment " + shipmentID + " status updated to " + status);
    }

    public void updateInventoryAfterShipment(int shipmentID) {
        System.out.println("Inventory updated after shipment " + shipmentID);
    }

    public String trackShipment(int shipmentID) {
        return "Tracking shipment " + shipmentID + ": " + status;
    }

    public void assignDistributor(int shipmentID, int distributorID) {
        this.shipmentID = shipmentID;
        System.out.println("Assign distributor " + distributorID + " to shipment " + shipmentID);
    }

    public void updateLocation(String location) {
        this.currentLocation = location;
        notifyObservers("Shipment location updated to " + location);
    }

    public void updateConditions(double temp, double humidity) {
        this.temperature = temp;
        this.humidity = humidity;
        notifyObservers("Shipment conditions updated");
    }

    public void markAsDelivered() {
        this.status  = ShipmentStatus.DELIVERED;
        this.arrivalDate = new Date();
    }

    public Date calculateETA() {
        return new Date();
    }

    public void reviewShipmentHistory(int shipmentID) {
        System.out.println("Review shipment history for " + shipmentID);
    }

    public boolean validateShipmentData() {
        return shipmentID > 0;
    }

    public void linkShipment(int shipmentID) {
        this.shipmentID = shipmentID;
    }

    public Customer getCustomer() {
        return customer;
    }
    
    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Distributor getDistributor() {
        return distributor;
    }

    public void setDistributor(Distributor distributor) {
        this.distributor = distributor;
    }

    public Logistics getLogistics() {
        return logistics;
    }

    public void setLogistics(Logistics logistics) {
        this.logistics = logistics;
    }

    public Retailer getRetailer() {
        return retailer;
    }

    public void setRetailer(Retailer retailer) {
        this.retailer = retailer;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }
    
    // rawan
   public enum ShipmentStatus {
    CREATED, IN_TRANSIT, DELIVERED, CANCELLED
}
    
   
   //rawan
  public void updateShipmentStatus(ShipmentStatus newStatus) {

    if (newStatus == null) {
        throw new IllegalArgumentException("Invalid status");
    }

    this.status = newStatus;

    notifyObservers("Shipment " + shipmentID + " updated to " + newStatus);

    if (newStatus == ShipmentStatus.DELIVERED) {
        markAsDelivered();
    }
}
   
   
    

    @Override
    public void registerObserver(ShipmentObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(ShipmentObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (ShipmentObserver observer : observers) {
            observer.update(message);
        }
    }

    @Override
    public String toString() {
        return "Shipment{" + "shipmentID=" + shipmentID + ", status='" + status + '\'' + '}';
    }
}
