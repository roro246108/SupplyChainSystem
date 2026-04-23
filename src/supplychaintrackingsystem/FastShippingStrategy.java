package supplychaintrackingsystem;

public class FastShippingStrategy implements ShippingStrategy {
    @Override
    public double calculateShippingCost(Shipment shipment) {
        return shipment == null ? 0.0 : Math.max(20.0, shipment.getWeight() * 2.5);
    }

    @Override
    public String estimateDeliveryTime(Shipment shipment) {
        return "1-2 business days";
    }
}
