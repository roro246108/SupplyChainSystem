package supplychaintrackingsystem;

import java.util.Date;
import java.util.List;

public class ShipmentBoundary {

    private final ShipmentController controller;

    public ShipmentBoundary(ShipmentController controller) {
        if (controller == null) {
            throw new NullPointerException("Shipment controller cannot be null.");
        }

        this.controller = controller;
    }

    public Shipment loadShipment(int shipmentID) {
        return controller.loadShipment(shipmentID);
    }

    public Shipment loadShipmentByOrderId(int orderID) {
        return controller.loadShipmentByOrderId(orderID);
    }

    public Shipment saveShipment(int orderID, Shipment shipment) {
        return controller.saveShipment(orderID, shipment);
    }

    public void updateShipmentStatus(int shipmentID, String status) {
        controller.updateShipmentStatus(shipmentID, status);
    }

    public void updateShipmentLocation(int shipmentID, String location) {
        controller.updateLocation(shipmentID, location);
    }

    public void updateShipmentConditions(int shipmentID, double temperature, double humidity, String notes) {
        controller.updateConditions(shipmentID, temperature, humidity, notes);
    }

    public void markDelivered(int shipmentID) {
        controller.markDelivered(shipmentID);
    }

    public Date calculateEta(int shipmentID) {
        return controller.calculateEtaDate(shipmentID);
    }

    public String trackShipment(int shipmentID) {
        return controller.trackShipment(shipmentID);
    }

    public List<String> loadTrackingNotes(int shipmentID) {
        return List.of(trackShipment(shipmentID).split("\n"));
    }
}
