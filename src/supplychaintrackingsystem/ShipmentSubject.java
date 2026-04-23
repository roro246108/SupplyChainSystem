package supplychaintrackingsystem;

public interface ShipmentSubject {
    void registerObserver(ShipmentObserver observer);

    void removeObserver(ShipmentObserver observer);

    void notifyObservers(String message);
}
