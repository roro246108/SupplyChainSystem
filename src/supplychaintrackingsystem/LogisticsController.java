package supplychaintrackingsystem;

import java.util.Date;

public class LogisticsController {

    private final Logistics logistics;
    private final ShipmentBoundary shipmentBoundary;
    private final UserController userController;

    public LogisticsController() {
        this(AppContext.getCurrentUser(), AppContext.shipmentBoundary(), AppContext.userController());
    }

    public LogisticsController(User user, ShipmentBoundary shipmentBoundary) {
        this(user, shipmentBoundary, AppContext.userController());
    }

    public LogisticsController(User user, ShipmentBoundary shipmentBoundary, UserController userController) {
        this.shipmentBoundary = shipmentBoundary == null ? AppContext.shipmentBoundary() : shipmentBoundary;
        this.userController = userController == null ? AppContext.userController() : userController;
        this.logistics = buildLogistics(user);
    }

    public Logistics getLogistics() {
        return logistics;
    }

    public Shipment loadShipment(int shipmentID) {
        Shipment shipment = shipmentBoundary.loadShipment(shipmentID);
        logistics.addShipment(shipment);
        return shipment;
    }

    public void scheduleDelivery(int shipmentID, Date deliveryDate) {
        Shipment shipment = loadShipment(shipmentID);
        logistics.scheduleDelivery(shipmentID, deliveryDate);
        shipment.setScheduledDeliveryDate(deliveryDate);
        shipment.setStatus("Scheduled");
        shipmentBoundary.saveShipment(shipment.getOrderID(), shipment);
    }

    public void monitorTransportConditions(int shipmentID) {
        Shipment shipment = loadShipment(shipmentID);
        logistics.monitorTransportConditions(shipmentID);
        shipmentBoundary.saveShipment(shipment.getOrderID(), shipment);
    }

    public void recordDeliveryDetails(int shipmentID, String receiverName, String deliveryLocation, String deliveryStatus) {
        Shipment shipment = loadShipment(shipmentID);
        logistics.recordDeliveryDetails(shipmentID, receiverName, deliveryLocation, deliveryStatus);
        shipmentBoundary.saveShipment(shipment.getOrderID(), shipment);
    }

    public void reportDeliveryIssue(int shipmentID, String issue) {
        Shipment shipment = loadShipment(shipmentID);
        logistics.reportDeliveryIssue(shipmentID, issue);
        shipmentBoundary.saveShipment(shipment.getOrderID(), shipment);
    }

    private Logistics buildLogistics(User user) {
        if (user instanceof Logistics existing) {
            return existing;
        }

        int userID = user == null ? 0 : user.getUserID();
        String name = user == null ? "" : user.getName();
        String email = user == null ? "" : user.getEmail();
        String password = user == null ? "" : user.getPassword();
        String role = user == null ? "Logistics" : user.getRole();
        String department = role;
        String assignedRegion = name;
        String contactNumber = userID > 0 ? userController.getUserPhone(userID) : "";

        return new Logistics(userID, name, email, password, role, userID, department, assignedRegion, contactNumber);
    }
}
