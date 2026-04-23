package supplychaintrackingsystem;

public class SystemController {
    private ShipmentSensorInterface sensor;

    public SystemController() {
    }

    public SystemController(ShipmentSensorInterface sensor) {
        this.sensor = sensor;
    }

    public void setSensor(ShipmentSensorInterface sensor) {
        this.sensor = sensor;
    }

    public void monitorShipment(Shipment shipment) {
        if (sensor != null) {
            sensor.collectShipmentData(shipment);
        }
    }
}
