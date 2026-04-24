package supplychaintrackingsystem;

import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Product {
    private int productID;
    private String productName;
    private String category;
    private Date manufactureDate;
    private Date expiryDate;
    private double unitPrice;
    private String status;
    private Inventory inventory;
    private Retailer retailer;
    private Order order;
    private Production production;
    private final List<Shipment> shipments = new ArrayList<>();
    private final List<ProductionRecord> productionRecords = new ArrayList<>();

    public Product() {
    }

    public Product(int productID, String productName, String category, Date manufactureDate,
                   Date expiryDate, double unitPrice) {
        this.productID = productID;
        this.productName = productName;
        this.category = category;
        this.manufactureDate = manufactureDate;
        this.expiryDate = expiryDate;
        this.unitPrice = unitPrice;
        this.status = "Created";
    }

    public Product(String productName, double basePrice, String description, String category) {
        this.productName = productName;
        this.unitPrice = basePrice;
        this.category = category;
        this.status = description;
    }

    public void createProduct(String productData) {
        this.status = "Created";
        this.category = productData;
    }

    public void createProduct(String productName, String category, Date manufactureDate, Date expiryDate, double unitPrice) {
        this.productName = productName;
        this.category = category;
        this.manufactureDate = manufactureDate;
        this.expiryDate = expiryDate;
        this.unitPrice = unitPrice;
        this.status = "Created";
    }

    public void updateProductStatus(String newStatus) {
        this.status = newStatus;
    }

    public void viewProductDetails() {
        System.out.println("Product ID: " + productID);
        System.out.println("Name: " + productName);
        System.out.println("Category: " + category);
        System.out.println("Manufacture Date: " + manufactureDate);
        System.out.println("Expiry Date: " + expiryDate);
        System.out.println("Unit Price: " + unitPrice);
        System.out.println("Status: " + status);
    }

    public boolean checkAvailability() {
        return "Available".equalsIgnoreCase(status)
                && (inventory == null || inventory.getStockLevel() > 0);
    }

    public void getLifecycleDetails() {
        System.out.println("Product lifecycle:");
        System.out.println("Manufactured on: " + manufactureDate);
        System.out.println("Stored in inventory: " + (inventory == null ? "No" : "Yes"));
        System.out.println("Shipment status: " + status);
        System.out.println("Current status: " + status);
        System.out.println("Expiry date: " + expiryDate);
    }

    public void addShipment(Shipment shipment) {
        if (shipment != null && !shipments.contains(shipment)) {
            shipments.add(shipment);
        }
    }

    public List<Shipment> getShipments() {
        return Collections.unmodifiableList(shipments);
    }

    public void addProductionRecord(ProductionRecord productionRecord) {
        if (productionRecord != null && !productionRecords.contains(productionRecord)) {
            productionRecords.add(productionRecord);
            productionRecord.setProduct(this);
        }
    }

    public List<ProductionRecord> getProductionRecords() {
        return Collections.unmodifiableList(productionRecords);
    }

    public void assignProduct(int productID, int retailerID) {
        this.productID = productID;
        this.status = "Assigned to Retailer";
        System.out.println("Assign product " + productID + " to retailer " + retailerID);
    }

    public int getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(Date manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getBasePrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Retailer getRetailer() {
        return retailer;
    }

    public void setRetailer(Retailer retailer) {
        this.retailer = retailer;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Production getProduction() {
        return production;
    }

    public void setProduction(Production production) {
        this.production = production;
    }

    @Override
    public String toString() {
        return "Product{"
                + "productID=" + productID
                + ", productName='" + productName + '\''
                + ", category='" + category + '\''
                + ", manufactureDate=" + manufactureDate
                + ", expiryDate=" + expiryDate
                + ", unitPrice=" + unitPrice
                + ", status='" + status + '\''
                + '}';
    }
}
