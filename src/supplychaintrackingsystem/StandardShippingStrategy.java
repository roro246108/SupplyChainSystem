package supplychaintrackingsystem;

public class StandardShippingStrategy implements ShippingStrategy {

    @Override
    public double calculateCost(double weight, double distance) {
        return (weight * 1.5) + (distance * 1.0) + 20;
    }

    @Override
    public String estimateTime(double distance) {
        if (distance <= 100) {
            return "2-3 days";
        } else if (distance <= 500) {
            return "4-6 days";
        } else {
            return "7-10 days";
        }
    }
}