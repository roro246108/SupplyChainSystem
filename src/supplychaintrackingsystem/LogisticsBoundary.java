package supplychaintrackingsystem;

import java.util.Date;

public class LogisticsBoundary {

    private final LogisticsController controller;

    public LogisticsBoundary(LogisticsController controller) {
        if (controller == null) {
            throw new NullPointerException("Logistics controller cannot be null.");
        }

        this.controller = controller;
    }

    public Logistics getLogistics() {
        return controller.getLogistics();
    }

    public Shipment loadShipment(int shipmentID) {
        return controller.loadShipment(shipmentID);
    }

    public void scheduleDelivery(int shipmentID, Date deliveryDate) {
        controller.scheduleDelivery(shipmentID, deliveryDate);
    }

    public void monitorTransportConditions(int shipmentID) {
        controller.monitorTransportConditions(shipmentID);
    }

    public void recordDeliveryDetails(int shipmentID, String receiverName, String deliveryLocation, String deliveryStatus) {
        controller.recordDeliveryDetails(shipmentID, receiverName, deliveryLocation, deliveryStatus);
    }

    public void reportDeliveryIssue(int shipmentID, String issue) {
        controller.reportDeliveryIssue(shipmentID, issue);
    }
}
