package supplychaintrackingsystem;

import java.util.ArrayList;
import java.util.Collections;
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
    private Date scheduledDeliveryDate;
    private Date deliveryRecordedDate;
    private String status;

    private String receiverName;
    private String deliveryLocation;
    private String deliveryStatus;
    private String deliveryIssue;

    private ShippingStrategy shippingStrategy;
    private final List<ShipmentObserver> shipmentObservers = new ArrayList<>();
    private final List<String> trackingHistory = new ArrayList<>();
    private final List<SensorData> sensorDataHistory = new ArrayList<>();

    private Product product;
    private Order order;
    private Customer customer;
    private Supplier supplier;
    private Retailer retailer;
    private Logistics logistics;
    private Distributor distributor;
    private Manufacturer manufacturer;

    public Shipment() {
        this.status = "Created";
        this.departureDate = new Date();
        addTrackingRecord("Shipment created.");
    }

    public Shipment(int shipmentID) {
        if (shipmentID <= 0) {
            throw new IllegalArgumentException("Shipment ID must be positive.");
        }

        this.shipmentID = shipmentID;
        this.status = "Created";
        this.departureDate = new Date();
        addTrackingRecord("Shipment created.");
    }

    public Shipment(Order order) {
        this();
        setOrder(order);
    }

    public Shipment(int shipmentID, String origin, String destination, double weight, double distance) {
        if (shipmentID <= 0) {
            throw new IllegalArgumentException("Shipment ID must be positive.");
        }
        if (origin == null || origin.trim().isEmpty()) {
            throw new IllegalArgumentException("Origin is required.");
        }
        if (destination == null || destination.trim().isEmpty()) {
            throw new IllegalArgumentException("Destination is required.");
        }
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight must be greater than 0.");
        }
        if (distance <= 0) {
            throw new IllegalArgumentException("Distance must be greater than 0.");
        }

        this.shipmentID = shipmentID;
        this.origin = origin.trim();
        this.destination = destination.trim();
        this.currentLocation = this.origin;
        this.weight = weight;
        this.distance = distance;
        this.status = "Created";
        this.departureDate = new Date();

        addTrackingRecord("Shipment created from " + this.origin + " to " + this.destination);
    }

    public Shipment(int shipmentID, String origin, String destination, String currentLocation, double weight,
                    double distance, double temperature, double humidity, Date departureDate, Date arrivalDate,
                    String status, ShippingStrategy shippingStrategy, Order order, Customer customer,
                    Supplier supplier, Distributor distributor, Logistics logistics, Retailer retailer,
                    Manufacturer manufacturer) {
        this(shipmentID, origin, destination, weight, distance);
        this.currentLocation = currentLocation == null || currentLocation.isBlank() ? origin.trim() : currentLocation.trim();
        this.temperature = temperature;
        this.humidity = humidity;
        this.departureDate = departureDate == null ? new Date() : departureDate;
        this.arrivalDate = arrivalDate;
        this.status = normalizeStatus(status);
        this.shippingStrategy = shippingStrategy;
        this.order = order;
        this.customer = customer;
        this.supplier = supplier;
        this.distributor = distributor;
        this.logistics = logistics;
        this.retailer = retailer;
        this.manufacturer = manufacturer;
        addTrackingRecord("Shipment initialized with full shipment details.");
    }

    public void setStrategy(ShippingStrategy strategy) {
        if (strategy == null) {
            throw new NullPointerException("Shipping strategy cannot be null.");
        }

        this.shippingStrategy = strategy;
        addTrackingRecord("Shipping strategy selected.");
    }

    public void sendShipmentToRetailer(int shipmentID, int retailerID) {
        checkShipmentID(shipmentID);

        if (retailerID <= 0) {
            throw new IllegalArgumentException("Retailer ID must be positive.");
        }

        validateShipmentData();

        this.status = "In Transit";
        this.deliveryStatus = "On the way";

        addTrackingRecord("Shipment sent to retailer ID: " + retailerID);
        notifyObservers("Shipment " + shipmentID + " is now in transit to retailer " + retailerID);
    }

    public double calculateShippingCost() {
        validateShipmentData();

        if (shippingStrategy == null) {
            return (distance * 0.5) + (weight * 2.0);
        }

        return shippingStrategy.calculateShippingCost(this);
    }

    public String estimateDeliveryTime() {
        if (distance <= 0) {
            throw new IllegalStateException("Distance must be set before estimating delivery time.");
        }

        if (shippingStrategy != null) {
            return shippingStrategy.estimateDeliveryTime(this);
        }

        if (distance <= 100) {
            return "1 day";
        }
        if (distance <= 500) {
            return "2-3 days";
        }
        return "4-7 days";
    }

    public Shipment createShipment(Shipment shipment) {
        if (shipment == null) {
            throw new NullPointerException("Shipment object cannot be null.");
        }

        this.shipmentID = shipment.shipmentID;
        this.origin = shipment.origin;
        this.destination = shipment.destination;
        this.currentLocation = shipment.currentLocation;
        this.weight = shipment.weight;
        this.distance = shipment.distance;
        this.temperature = shipment.temperature;
        this.humidity = shipment.humidity;
        this.departureDate = shipment.departureDate;
        this.arrivalDate = shipment.arrivalDate;
        this.scheduledDeliveryDate = shipment.scheduledDeliveryDate;
        this.deliveryRecordedDate = shipment.deliveryRecordedDate;
        this.status = "Created";
        this.receiverName = shipment.receiverName;
        this.deliveryLocation = shipment.deliveryLocation;
        this.deliveryStatus = shipment.deliveryStatus;
        this.deliveryIssue = shipment.deliveryIssue;
        this.shippingStrategy = shipment.shippingStrategy;
        this.product = shipment.product;
        this.order = shipment.order;
        this.customer = shipment.customer;
        this.supplier = shipment.supplier;
        this.retailer = shipment.retailer;
        this.logistics = shipment.logistics;
        this.distributor = shipment.distributor;
        this.manufacturer = shipment.manufacturer;
        this.sensorDataHistory.clear();
        this.sensorDataHistory.addAll(shipment.sensorDataHistory);
        this.trackingHistory.clear();

        validateShipmentData();
        addTrackingRecord("Shipment data copied and created.");
        return this;
    }

    public Shipment createShipment(Order order) {
        setOrder(order);
        addTrackingRecord("Shipment linked to order " + order.getOrderID() + ".");
        return this;
    }

    public void updateStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be empty.");
        }

        this.status = normalizeStatus(status);
        addTrackingRecord("Status updated to: " + this.status);
        notifyObservers("Shipment " + shipmentID + " status updated to " + this.status);
    }

    public void updateShipmentStatus(int shipmentID, String status) {
        checkShipmentID(shipmentID);
        updateStatus(status);
    }

    public void receiveShipment(int shipmentID) {
        checkShipmentID(shipmentID);

        if ("Delivered".equalsIgnoreCase(status)) {
            throw new IllegalStateException("Shipment is already delivered.");
        }

        this.status = "Received";
        this.arrivalDate = new Date();
        this.deliveryStatus = "Received";

        addTrackingRecord("Shipment received.");
        notifyObservers("Shipment " + shipmentID + " has been received.");
    }

    @Override
    public void registerObserver(ShipmentObserver observer) {
        if (observer == null) {
            throw new NullPointerException("Observer cannot be null.");
        }

        if (!shipmentObservers.contains(observer)) {
            shipmentObservers.add(observer);
        }
    }

    @Override
    public void removeObserver(ShipmentObserver observer) {
        if (observer == null) {
            throw new NullPointerException("Observer cannot be null.");
        }

        shipmentObservers.remove(observer);
    }

    @Override
public void notifyObservers(String message) {

    if (message == null || message.trim().isEmpty()) {
        return;
    }

    for (ShipmentObserver observer : shipmentObservers) {
        try {
            observer.update(message);
        } catch (Exception e) {
            System.out.println("Observer update failed.");
        }
    }
}

public void setLatestSensorData(SensorData latestSensorData) {

    if (latestSensorData == null) {
        throw new NullPointerException("Sensor data cannot be null.");
    }

    updateConditions(latestSensorData);
}

    public void updateLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be empty.");
        }

        this.currentLocation = location.trim();
        addTrackingRecord("Location updated to: " + this.currentLocation);
        notifyObservers("Shipment " + shipmentID + " location updated to " + this.currentLocation);
    }

    public void updateConditions(double temp, double humidity) {
        if (temp < -50 || temp > 60) {
            throw new IllegalArgumentException("Temperature value is unrealistic.");
        }

        if (humidity < 0 || humidity > 100) {
            throw new IllegalArgumentException("Humidity must be between 0 and 100.");
        }

        this.temperature = temp;
        this.humidity = humidity;

        SensorData data = new SensorData(temp, humidity, currentLocation, new Date());
        sensorDataHistory.add(data);

        addTrackingRecord("Conditions updated: temperature=" + temp + ", humidity=" + humidity);
        notifyObservers("Shipment " + shipmentID + " conditions updated.");
    }

    public void updateConditions(SensorData sensorData) {
        if (sensorData == null) {
            throw new NullPointerException("Sensor data cannot be null.");
        }

        this.temperature = sensorData.getTemperature();
        this.humidity = sensorData.getHumidity();

        if (sensorData.getLocation() != null && !sensorData.getLocation().isBlank()) {
            this.currentLocation = sensorData.getLocation();
        }

        sensorDataHistory.add(sensorData);
        addTrackingRecord("Conditions updated from sensor data.");
        notifyObservers("Shipment " + shipmentID + " conditions updated.");
    }

    public void markAsDelivered() {
        if ("Delivered".equalsIgnoreCase(status)) {
            throw new IllegalStateException("Shipment is already delivered.");
        }

        this.status = "Delivered";
        this.deliveryStatus = "Delivered";
        this.arrivalDate = new Date();

        if (deliveryRecordedDate == null) {
            deliveryRecordedDate = new Date();
        }

        addTrackingRecord("Shipment delivered.");
        notifyObservers("Shipment " + shipmentID + " has been delivered.");
    }

    public Date calculateETA() {
        if (scheduledDeliveryDate != null) {
            return scheduledDeliveryDate;
        }

        if (distance <= 0) {
            throw new IllegalStateException("Distance must be set before calculating ETA.");
        }

        long days;
        if (distance <= 100) {
            days = 1;
        } else if (distance <= 500) {
            days = 3;
        } else {
            days = 7;
        }

        return new Date(System.currentTimeMillis() + days * 24L * 60L * 60L * 1000L);
    }

    public void reviewShipmentHistory(int shipmentID) {
        checkShipmentID(shipmentID);

        if (trackingHistory.isEmpty()) {
            System.out.println("No tracking history found.");
            return;
        }

        System.out.println("Shipment History for Shipment " + shipmentID + ":");
        for (String record : trackingHistory) {
            System.out.println("- " + record);
        }
    }

    public boolean validateShipmentData() {
        if (shipmentID <= 0) {
            throw new IllegalStateException("Shipment ID is invalid.");
        }
        if (origin == null || origin.trim().isEmpty()) {
            throw new IllegalStateException("Origin is missing.");
        }
        if (destination == null || destination.trim().isEmpty()) {
            throw new IllegalStateException("Destination is missing.");
        }
        if (weight <= 0) {
            throw new IllegalStateException("Weight must be greater than 0.");
        }
        if (distance <= 0) {
            throw new IllegalStateException("Distance must be greater than 0.");
        }

        return true;
    }

    public void linkShipment(int shipmentID) {
        if (shipmentID <= 0) {
            throw new IllegalArgumentException("Shipment ID must be positive.");
        }

        this.shipmentID = shipmentID;
        addTrackingRecord("Shipment linked with ID: " + shipmentID);
    }

    public void addTrackingRecord() {
        addTrackingRecord("Tracking record added.");
    }

    public void addTrackingRecord(String record) {
        if (record == null || record.trim().isEmpty()) {
            throw new IllegalArgumentException("Tracking record cannot be empty.");
        }

        trackingHistory.add(new Date() + " - " + record.trim());
    }

    public void updateInventoryAfterShipment(int shipmentID) {
        checkShipmentID(shipmentID);
        addTrackingRecord("Inventory updated after shipment.");
    }

    public String trackShipment(int shipmentID) {
        checkShipmentID(shipmentID);
        return "Tracking shipment " + shipmentID + ": " + status;
    }

    public void assignDistributor(int shipmentID, int distributorID) {
        checkShipmentID(shipmentID);

        if (distributorID <= 0) {
            throw new IllegalArgumentException("Distributor ID must be positive.");
        }

        addTrackingRecord("Distributor " + distributorID + " assigned to shipment.");
    }

    private void checkShipmentID(int shipmentID) {
        if (this.shipmentID != shipmentID) {
            throw new IllegalArgumentException("Shipment ID does not match.");
        }
    }

    private String normalizeStatus(String status) {
        String trimmed = status.trim();

        if ("CREATED".equalsIgnoreCase(trimmed) || "NEW".equalsIgnoreCase(trimmed)) {
            return "Created";
        }
        if ("IN TRANSIT".equalsIgnoreCase(trimmed) || "IN_TRANSIT".equalsIgnoreCase(trimmed) || "SCHEDULED".equalsIgnoreCase(trimmed)) {
            return "In Transit";
        }
        if ("DELIVERED".equalsIgnoreCase(trimmed) || "RECEIVED".equalsIgnoreCase(trimmed)) {
            return "Delivered";
        }
        if ("CANCELLED".equalsIgnoreCase(trimmed)) {
            return "Cancelled";
        }
        if ("ISSUE REPORTED".equalsIgnoreCase(trimmed) || "ISSUE_REPORTED".equalsIgnoreCase(trimmed)) {
            return "Issue Reported";
        }

        return trimmed;
    }

    public SensorData getLatestSensorData() {
        if (sensorDataHistory.isEmpty()) {
            return null;
        }

        return sensorDataHistory.get(sensorDataHistory.size() - 1);
    }

    public int getShipmentID() {
        return shipmentID;
    }

    public void setShipmentID(int shipmentID) {
        if (shipmentID <= 0) {
            throw new IllegalArgumentException("Shipment ID must be positive.");
        }
        this.shipmentID = shipmentID;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        if (origin == null || origin.trim().isEmpty()) {
            throw new IllegalArgumentException("Origin cannot be empty.");
        }
        this.origin = origin.trim();
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        if (destination == null || destination.trim().isEmpty()) {
            throw new IllegalArgumentException("Destination cannot be empty.");
        }
        this.destination = destination.trim();
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        if (currentLocation == null || currentLocation.trim().isEmpty()) {
            throw new IllegalArgumentException("Current location cannot be empty.");
        }
        this.currentLocation = currentLocation.trim();
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight must be greater than 0.");
        }
        this.weight = weight;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("Distance must be greater than 0.");
        }
        this.distance = distance;
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

    public void setDepartureDate(Date departureDate) {
        if (departureDate == null) {
            throw new NullPointerException("Departure date cannot be null.");
        }
        this.departureDate = departureDate;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        if (arrivalDate == null) {
            throw new NullPointerException("Arrival date cannot be null.");
        }
        this.arrivalDate = arrivalDate;
    }

    public Date getScheduledDeliveryDate() {
        return scheduledDeliveryDate;
    }

    public void setScheduledDeliveryDate(Date scheduledDeliveryDate) {
        if (scheduledDeliveryDate == null) {
            throw new NullPointerException("Scheduled delivery date cannot be null.");
        }
        this.scheduledDeliveryDate = scheduledDeliveryDate;
    }

    public Date getDeliveryRecordedDate() {
        return deliveryRecordedDate;
    }

    public void setDeliveryRecordedDate(Date deliveryRecordedDate) {
        if (deliveryRecordedDate == null) {
            throw new NullPointerException("Delivery recorded date cannot be null.");
        }
        this.deliveryRecordedDate = deliveryRecordedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        updateStatus(status);
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        if (receiverName == null || receiverName.trim().isEmpty()) {
            throw new IllegalArgumentException("Receiver name cannot be empty.");
        }
        this.receiverName = receiverName.trim();
    }

    public String getDeliveryLocation() {
        return deliveryLocation;
    }

    public void setDeliveryLocation(String deliveryLocation) {
        if (deliveryLocation == null || deliveryLocation.trim().isEmpty()) {
            throw new IllegalArgumentException("Delivery location cannot be empty.");
        }
        this.deliveryLocation = deliveryLocation.trim();
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        if (deliveryStatus == null || deliveryStatus.trim().isEmpty()) {
            throw new IllegalArgumentException("Delivery status cannot be empty.");
        }
        this.deliveryStatus = deliveryStatus.trim();
    }

    public String getDeliveryIssue() {
        return deliveryIssue;
    }

    public void setDeliveryIssue(String deliveryIssue) {
        if (deliveryIssue == null || deliveryIssue.trim().isEmpty()) {
            throw new IllegalArgumentException("Delivery issue cannot be empty.");
        }
        this.deliveryIssue = deliveryIssue.trim();
    }

    public ShippingStrategy getShippingStrategy() {
        return shippingStrategy;
    }

    public List<ShipmentObserver> getShipmentObservers() {
        return Collections.unmodifiableList(shipmentObservers);
    }

    public List<String> getTrackingHistory() {
        return Collections.unmodifiableList(trackingHistory);
    }

    public List<SensorData> getSensorDataHistory() {
        return Collections.unmodifiableList(sensorDataHistory);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        if (product == null) {
            throw new NullPointerException("Product cannot be null.");
        }

        this.product = product;

        if (!product.getShipments().contains(this)) {
            product.addShipment(this);
        }
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        if (order == null) {
            throw new NullPointerException("Order cannot be null.");
        }
        this.order = order;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        if (this.customer != null && this.customer != customer) {
            removeObserver(this.customer);
        }

        this.customer = customer;

        if (customer != null) {
            registerObserver(customer);
        }
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Retailer getRetailer() {
        return retailer;
    }

    public void setRetailer(Retailer retailer) {
        if (retailer == null) {
            throw new NullPointerException("Retailer cannot be null.");
        }
        this.retailer = retailer;
    }

    public Logistics getLogistics() {
        return logistics;
    }

    public void setLogistics(Logistics logistics) {
        if (logistics == null) {
            this.logistics = null;
            return;
        }

        this.logistics = logistics;

        if (!logistics.getShipments().contains(this)) {
            logistics.addShipment(this);
        }
    }

    public Distributor getDistributor() {
        return distributor;
    }

    public void setDistributor(Distributor distributor) {
        this.distributor = distributor;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

   
}
