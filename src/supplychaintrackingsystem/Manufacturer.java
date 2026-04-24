package supplychaintrackingsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Manufacturer extends User {

    private String facilityLocation;
    private Regulator regulator;

    // Multiplicity: Manufacturer 1 -> * Retailer
    private final List<Retailer> retailers = new ArrayList<>();

    // Multiplicity: Manufacturer 1 -> * Shipment
    private final List<Shipment> shipments = new ArrayList<>();

    private final List<Product> products = new ArrayList<>();
    private final List<ProductionRecord> productionRecords = new ArrayList<>();
    private final List<String> maintenanceRequests = new ArrayList<>();

    public Manufacturer(int userID, String name, String email, String password, String role,
                        String facilityLocation) {
        super(userID, name, email, password, role);

        if (facilityLocation == null || facilityLocation.isBlank()) {
            throw new IllegalArgumentException("Facility location cannot be empty.");
        }

        this.facilityLocation = facilityLocation.trim();
    }

    public void monitorStorageConditions() {
        if (facilityLocation == null || facilityLocation.isBlank()) {
            throw new IllegalStateException("Cannot monitor storage conditions without facility location.");
        }

        System.out.println("Monitoring storage conditions at " + facilityLocation);
        System.out.println("- Temperature: Normal");
        System.out.println("- Humidity: Normal");
        System.out.println("- Storage safety: Stable");
    }

    public void requestMaintenance() {
        String request = "Maintenance requested for facility: " + facilityLocation;
        maintenanceRequests.add(request);
        System.out.println(request);

        if (regulator != null) {
            System.out.println("Regulator has been notified about maintenance request.");
        }
    }

    public void startProduction(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }

        products.add(product);

        ProductionRecord record = new ProductionRecord(
                "Production started for product: " + product.getProductName()
        );

        productionRecords.add(record);

        System.out.println("Starting production for " + product.getProductName());
    }

    public List<ProductionRecord> viewProductionRecords() {
        return Collections.unmodifiableList(productionRecords);
    }

    public void completeProduction(Product product, int producedQuantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }

        if (producedQuantity <= 0) {
            throw new IllegalArgumentException("Produced quantity must be positive.");
        }

        productionRecords.add(new ProductionRecord(
                "Completed production for " + product.getProductName()
                        + " | Quantity: " + producedQuantity
        ));

        System.out.println("Production completed for " + product.getProductName());
    }

    public boolean assignProductToRetailer(Product product, Retailer retailer) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }

        if (retailer == null) {
            throw new IllegalArgumentException("Retailer cannot be null.");
        }

        if (!retailers.contains(retailer)) {
            addRetailer(retailer);
        }

        System.out.println("Product " + product.getProductName()
                + " assigned to retailer " + retailer.getName());

        return true;
    }

    public String viewSupplyStatus() {
        int availableProducts = 0;
        int outOfStockProducts = 0;
        int totalQuantity = 0;

        for (Product product : products) {
            if (isProductAvailable(product)) {
                availableProducts++;
            } else {
                outOfStockProducts++;
            }

            totalQuantity += 1;
        }

        return "Supply Status:"
                + "\nFacility Location: " + facilityLocation
                + "\nTotal Products: " + products.size()
                + "\nAvailable Products: " + availableProducts
                + "\nOut of Stock Products: " + outOfStockProducts
                + "\nTotal Quantity: " + totalQuantity
                + "\nTotal Shipments: " + shipments.size();
    }

    public void scheduleDelivery(Shipment shipment, Retailer retailer) {
        if (shipment == null) {
            throw new IllegalArgumentException("Shipment cannot be null.");
        }

        if (retailer == null) {
            throw new IllegalArgumentException("Retailer cannot be null.");
        }

        addRetailer(retailer);
        addShipment(shipment);

        System.out.println("Delivery scheduled from " + facilityLocation
                + " to retailer " + retailer.getName());
    }

    public List<Retailer> getRetailers() {
        return Collections.unmodifiableList(retailers);
    }

    public List<Shipment> getShipments() {
        return Collections.unmodifiableList(shipments);
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public List<String> getMaintenanceRequests() {
        return Collections.unmodifiableList(maintenanceRequests);
    }

    public void addRetailer(Retailer retailer) {
        if (retailer == null) {
            throw new IllegalArgumentException("Retailer cannot be null.");
        }

        if (retailers.contains(retailer)) {
            return;
        }

        retailers.add(retailer);
    }

    public void addShipment(Shipment shipment) {
        if (shipment == null) {
            throw new IllegalArgumentException("Shipment cannot be null.");
        }

        if (shipments.contains(shipment)) {
            return;
        }

        shipments.add(shipment);
        shipment.setManufacturer(this);
    }

    public Regulator getRegulator() {
        return regulator;
    }

    public void setRegulator(Regulator regulator) {
        if (regulator == null) {
            throw new IllegalArgumentException("Regulator cannot be null.");
        }

        this.regulator = regulator;
    }

    public String getFacilityLocation() {
        return facilityLocation;
    }

    public void setFacilityLocation(String facilityLocation) {
        if (facilityLocation == null || facilityLocation.isBlank()) {
            throw new IllegalArgumentException("Facility location cannot be empty.");
        }

        this.facilityLocation = facilityLocation.trim();
    }

    private boolean isProductAvailable(Product product) {
        return product != null && !"OUT_OF_STOCK".equalsIgnoreCase(product.getStatus());
    }
}
