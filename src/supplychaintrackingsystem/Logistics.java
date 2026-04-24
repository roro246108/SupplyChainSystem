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
    private ShipmentSensorInterface sensor;
    private NotificationService notificationService;
    private static final double MAX_SAFE_TEMPERATURE = 8.0;
    private static final double MIN_SAFE_TEMPERATURE = 0.0;
    private static final double MAX_SAFE_HUMIDITY = 75.0;
    private static final double MIN_SAFE_HUMIDITY = 20.0;
    private final List<Shipment> shipments = new ArrayList<>();
    private Distributor distributor;

    public Logistics(int userID, String name, String email, String password, String role,
                     int logisticsID, String department, String assignedRegion, String contactNumber) {
        super(userID, name, email, password, role);
        this.logisticsID = logisticsID;
        this.department = department;
        this.assignedRegion = assignedRegion;
        this.contactNumber = contactNumber;
        this.sensor = new SensorAdapter(new ExternalSensor());
        this.notificationService = new NotificationService();
    }

    public int getLogisticsID() {
        return logisticsID;
    }

    public void setLogisticsID(int logisticsID) {
        this.logisticsID = logisticsID;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAssignedRegion() {
        return assignedRegion;
    }

    public void setAssignedRegion(String assignedRegion) {
        this.assignedRegion = assignedRegion;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
    

    public void scheduleDelivery(int shipmentID, Date deliveryDate) {
        Shipment shipment = findShipment(shipmentID);
        if (shipment == null) {
            System.out.println("Shipment " + shipmentID + " was not found.");
            return;
        }
        if (deliveryDate == null) {
            System.out.println("Delivery date is required for shipment " + shipmentID + ".");
            return;
        }
        shipment.setScheduledDeliveryDate(deliveryDate);
        shipment.updateShipmentStatus(shipmentID, "Scheduled");
        System.out.println("Delivery scheduled for shipment " + shipmentID + " on " + deliveryDate);
    }

    public void monitorTransportConditions(int shipmentID) {
        Shipment shipment = findShipment(shipmentID);
        if (shipment == null) {
            System.out.println("Shipment " + shipmentID + " was not found.");
            return;
        }

        if (sensor != null) {
            sensor.collectShipmentData(shipment);
        }

        SensorData sensorData = shipment.getLatestSensorData();
        if (sensorData == null) {
            System.out.println("No sensor data available for shipment " + shipmentID + ".");
            return;
        }

        double temperature = sensorData.getTemperature();
        double humidity = sensorData.getHumidity();
        boolean safeTemperature = temperature >= MIN_SAFE_TEMPERATURE && temperature <= MAX_SAFE_TEMPERATURE;
        boolean safeHumidity = humidity >= MIN_SAFE_HUMIDITY && humidity <= MAX_SAFE_HUMIDITY;

        if (safeTemperature && safeHumidity) {
            System.out.println("Shipment " + shipmentID + " transport conditions are normal.");
            return;
        }

        StringBuilder warning = new StringBuilder("Warning: Shipment ")
                .append(shipmentID)
                .append(" is outside safe transport conditions.");
        if (!safeTemperature) {
            warning.append(" Temperature=").append(temperature);
        }
        if (!safeHumidity) {
            warning.append(" Humidity=").append(humidity);
        }

        String message = warning.toString();
        System.out.println(message);
        shipment.setStatus("Issue Reported");
        shipment.setDeliveryIssue(message);
        if (notificationService != null) {
            notificationService.notifyAdmin(message);
        }
        shipment.notifyObservers(message);
    }

    public void recordDeliveryDetails(int shipmentID, String receiverName, String deliveryLocation, String deliveryStatus) {
        Shipment shipment = findShipment(shipmentID);
        if (shipment == null) {
            System.out.println("Shipment " + shipmentID + " was not found.");
            return;
        }

        shipment.setReceiverName(receiverName);
        shipment.setDeliveryLocation(deliveryLocation);
        shipment.setDeliveryStatus(deliveryStatus);
        shipment.setDeliveryRecordedDate(new Date());
        shipment.markAsDelivered();
        shipment.notifyObservers("Delivery details recorded for shipment " + shipmentID);
        System.out.println("Delivery details recorded for shipment " + shipmentID);
    }

    public void reportDeliveryIssue(int shipmentID, String issue) {
        Shipment shipment = findShipment(shipmentID);
        if (shipment == null) {
            System.out.println("Shipment " + shipmentID + " was not found.");
            return;
        }

        shipment.setDeliveryIssue(issue);
        shipment.setDeliveryStatus("Issue Reported");
        shipment.setStatus("Issue Reported");
        String message = "Shipment " + shipmentID + " issue: " + issue;
        System.out.println(message);
        if (notificationService != null) {
            notificationService.notifyAdmin(message);
        }
        shipment.notifyObservers(message);
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

    public void setSensor(ShipmentSensorInterface sensor) {
        this.sensor = sensor;
    }

    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    private Shipment findShipment(int shipmentID) {
        for (Shipment shipment : shipments) {
            if (shipment.getShipmentID() == shipmentID) {
                return shipment;
            }
        }
        return null;
    }
}
