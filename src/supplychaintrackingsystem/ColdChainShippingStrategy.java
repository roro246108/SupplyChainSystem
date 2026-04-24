package supplychaintrackingsystem;

public class ColdChainShippingStrategy implements ShippingStrategy {
    @Override
    public double calculateShippingCost(Shipment shipment) {
        return shipment == null ? 0.0 : Math.max(30.0, shipment.getWeight() * 3.5);
    }

    @Override
    public String estimateDeliveryTime(Shipment shipment) {
        return "2-4 business days";
    }
}
