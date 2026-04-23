package supplychaintrackingsystem;

public interface ShippingStrategy {
    double calculateShippingCost(Shipment shipment);

    String estimateDeliveryTime(Shipment shipment);
}
