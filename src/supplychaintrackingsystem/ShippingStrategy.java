package supplychaintrackingsystem;

public interface ShippingStrategy {
    double calculateCost(double weight, double distance);

    String estimateTime(double distance);
}
