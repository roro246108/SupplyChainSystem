package supplychaintrackingsystem;

public class StandardShippingStrategy implements ShippingStrategy {
    @Override
    public double calculateShippingCost(Shipment shipment) {
        return shipment == null ? 0.0 : Math.max(10.0, shipment.getWeight() * 1.5);
    }

    @Override
    public String estimateDeliveryTime(Shipment shipment) {
        return "3-5 business days";
    }
}
