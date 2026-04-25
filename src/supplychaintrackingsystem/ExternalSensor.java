package supplychaintrackingsystem;

import java.util.Date;

public class ExternalSensor { //nanyyyyyyyyy
    public double readTemperature() {
        return 5.0;
    }

    public double readHumidity() {
        return 45.0;
    }

    public String readLocation() {
        return "In Transit";
    }

    public SensorData readSensorData() {
        return new SensorData(readTemperature(), readHumidity(), readLocation(), new Date());
    }

    public void sendSensorData(Shipment shipment) {
        if (shipment != null) {
            shipment.setLatestSensorData(readSensorData());
        }
    }
}////nanyyyyyyyyyyyy
