/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package supplychaintrackingsystem;

import supplychaintrackingsystem.Shipment;
import supplychaintrackingsystem.ShipmentController;
/**
 *
 * @author Rawan
 */
public class DistributorBoundary {
    
     private ShipmentController controller;

    public DistributorBoundary(ShipmentController controller) {
        this.controller = controller;
    }

    public Shipment requestAddShipment(int id, String origin,
            String destination, double weight, double distance) {

        return controller.createShipment(id, origin, destination, weight, distance);
    }

    public void requestSendProducts(int shipmentID, int retailerID) {
        controller.sendShipment(shipmentID, retailerID);
    }

    public void requestReceiveProducts(int shipmentID) {
        controller.receiveShipment(shipmentID);
    }

    public void requestViewShipments(int shipmentID) {
        controller.trackShipment(shipmentID);
    }

    public void requestUpdateStatus(int shipmentID, String status) {
        controller.updateShipmentStatus(shipmentID, status);
    }

    public void requestUpdateLocation(int shipmentID, String location) {
        controller.updateLocation(shipmentID, location);
    }
    
    
    
}
