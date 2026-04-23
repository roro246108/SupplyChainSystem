package supplychaintrackingsystem;

public class SensorAdapter implements ShipmentSensorInterface {
    private final ExternalSensor sensor;

    public SensorAdapter(ExternalSensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public void collectShipmentData(Shipment shipment) {
        sensor.sendSensorData(shipment);
    }
}
