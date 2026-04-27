package supplychaintrackingsystem;

public class ColdChainShippingStrategy implements ShippingStrategy {

    @Override
    public double calculateCost(double weight, double distance) {
        return (weight * 4.0) + (distance * 2.2) + 120;
    }

    @Override
    public String estimateTime(double distance) {
        if (distance <= 100) {
            return "1 day with temperature control";
        } else if (distance <= 500) {
            return "2-3 days with temperature control";
        } else {
            return "3-5 days with temperature control";
        }
    }
}