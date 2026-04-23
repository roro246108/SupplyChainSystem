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
    private String status;
    private ShippingStrategy shippingStrategy;
    private final List<ShipmentObserver> observers = new ArrayList<>();
    private Order order;
    private Customer customer;
    private Supplier supplier;
    private Distributor distributor;
    private Logistics logistics;
    private Retailer retailer;
    private Manufacturer manufacturer;

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

    public String getStatus() {
        return status;
    }

    public void setStrategy(ShippingStrategy strategy) {
        this.shippingStrategy = strategy;
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

    public void updateShipmentStatus(int shipmentID, String status) {
        this.shipmentID = shipmentID;
        this.status = status;
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
        this.status = "DELIVERED";
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

    public void setCustomer(Customer customer) {
        this.customer = customer;
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
