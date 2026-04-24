package supplychaintrackingsystem;

import java.util.Date;

public class SensorData {
    private double temperature;
    private double humidity;
    private String location;
    private Date recordedTime;

    public SensorData(double temperature, double humidity, String location, Date recordedTime) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.location = location;
        this.recordedTime = recordedTime;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getRecordedTime() {
        return recordedTime;
    }

    public void setRecordedTime(Date recordedTime) {
        this.recordedTime = recordedTime;
    }

    @Override
    public String toString() {
        return "SensorData{"
                + "temperature=" + temperature
                + ", humidity=" + humidity
                + ", location='" + location + '\''
                + ", recordedTime=" + recordedTime
                + '}';
    }
}
