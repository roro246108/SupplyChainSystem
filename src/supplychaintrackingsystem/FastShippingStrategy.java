package supplychaintrackingsystem;

public class FastShippingStrategy implements ShippingStrategy {
    @Override
    public double calculateCost(double weight, double distance) {
        return (weight * 2.5) + (distance * 1.8) + 50;
    }

    @Override
    public String estimateTime(double distance) {
        if (distance <= 100) {
            return "Same day delivery";
        } else if (distance <= 500) {
            return "1-2 days";
        } else {
            return "2-3 days";
        }
    }
}
