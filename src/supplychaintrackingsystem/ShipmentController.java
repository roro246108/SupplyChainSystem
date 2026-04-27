package supplychaintrackingsystem;

import java.util.HashMap;
import java.util.Map;

public class ShipmentController {

    // Simulated in-memory storage. This can be replaced with a database later.
    private final Map<Integer, Shipment> shipments = new HashMap<>();

    public Shipment createShipment(int id, String origin, String destination, double weight, double distance) {
        try {
            Shipment shipment = new Shipment(id, origin, destination, weight, distance);
            shipments.put(id, shipment);

            System.out.println("Shipment created successfully.");
            return shipment;
        } catch (Exception e) {
            System.out.println("Error creating shipment: " + e.getMessage());
            return null;
        }
    }

    public void sendShipment(int shipmentID, int retailerID) {
        try {
            Shipment shipment = getShipment(shipmentID);
            shipment.sendShipmentToRetailer(shipmentID, retailerID);
        } catch (Exception e) {
            System.out.println("Error sending shipment: " + e.getMessage());
        }
    }

    public void updateShipmentStatus(int shipmentID, String status) {
        try {
            Shipment shipment = getShipment(shipmentID);
            shipment.updateShipmentStatus(shipmentID, status);
        } catch (Exception e) {
            System.out.println("Error updating status: " + e.getMessage());
        }
    }

    public void updateLocation(int shipmentID, String location) {
        try {
            Shipment shipment = getShipment(shipmentID);
            shipment.updateLocation(location);
        } catch (Exception e) {
            System.out.println("Error updating location: " + e.getMessage());
        }
    }

    public void updateConditions(int shipmentID, double temp, double humidity) {
        try {
            Shipment shipment = getShipment(shipmentID);
            shipment.updateConditions(temp, humidity);
        } catch (Exception e) {
            System.out.println("Error updating conditions: " + e.getMessage());
        }
    }

    public void receiveShipment(int shipmentID) {
        try {
            Shipment shipment = getShipment(shipmentID);
            shipment.receiveShipment(shipmentID);
        } catch (Exception e) {
            System.out.println("Error receiving shipment: " + e.getMessage());
        }
    }

    public void deliverShipment(int shipmentID) {
        try {
            Shipment shipment = getShipment(shipmentID);
            shipment.markAsDelivered();
        } catch (Exception e) {
            System.out.println("Error delivering shipment: " + e.getMessage());
        }
    }

    public void trackShipment(int shipmentID) {
        try {
            Shipment shipment = getShipment(shipmentID);
            shipment.reviewShipmentHistory(shipmentID);
        } catch (Exception e) {
            System.out.println("Error tracking shipment: " + e.getMessage());
        }
    }

    public double calculateCost(int shipmentID) {
        try {
            Shipment shipment = getShipment(shipmentID);
            return shipment.calculateShippingCost();
        } catch (Exception e) {
            System.out.println("Error calculating cost: " + e.getMessage());
            return 0;
        }
    }

    public String estimateTime(int shipmentID) {
        try {
            Shipment shipment = getShipment(shipmentID);
            return shipment.estimateDeliveryTime();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private Shipment getShipment(int shipmentID) {
        Shipment shipment = shipments.get(shipmentID);

        if (shipment == null) {
            throw new IllegalArgumentException("Shipment not found.");
        }

        return shipment;
    }
}
