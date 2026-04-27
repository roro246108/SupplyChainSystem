package supplychaintrackingsystem;

public class Inventory {
    private int inventoryID;
    private int productID;
    private int stockLevel;
    private int quantity;
    private String stockData;
    private int reorderThreshold;
    private String productName;
    private String warehouseLocation;
    private String availability;
    private String stockCondition;
    private String lowStockNotes;
    private String lastUpdated;
    private InventoryState currentState;
    private NotificationService notificationService;
    private Product product;

    public Inventory() {
        this.stockLevel = 0;
        this.quantity = 0;
        this.reorderThreshold = 10;
        this.availability = "Not Available";
        this.stockCondition = "Out of Stock";
        this.lowStockNotes = "No low stock alerts.";
        this.lastUpdated = "";
        this.currentState = new OutOfStockState();
        this.notificationService = new NotificationService();
        this.stockData = "Inventory initialized";
    }

    public void setState(InventoryState state) {
        this.currentState = state;
    }

    public InventoryState getCurrentState() {
        return currentState;
    }

    public void addStock(int quantity) {
        currentState.addStock(this, quantity);
    }

    public void removeStock(int quantity) {
        if (quantity <= 0) {
            System.out.println("Quantity must be positive.");
            return;
        }
        if (quantity > stockLevel) {
            System.out.println("Not enough stock available.");
            return;
        }

        decreaseQuantity(quantity);
        updateStock();
    }

    public void updateStock() {
        currentState.updateStock(this);
    }

    public boolean checkAvailability() {
        return currentState.checkAvailability(this);
    }

    public void trackStockLevels() {
        System.out.println("Current stock level: " + stockLevel);
        System.out.println("Current inventory state: " + currentState.getClass().getSimpleName());
    }

    public void detectLowStock() {
        currentState.detectLowStock(this);
    }

    public void generateLowStockAlert() {
        String message = "Low stock alert: inventory is below reorder threshold.";
        if (notificationService != null) {
            notificationService.notifyAdmin(message);
        } else {
            System.out.println(message);
        }
    }

    public boolean isBelowThreshold() {
        return stockLevel <= reorderThreshold;
    }

    public void increaseQuantity(int qty) {
        if (qty > 0) {
            this.stockLevel += qty;
            this.quantity = this.stockLevel;
            this.stockData = "Added " + qty + " units. Current stock: " + stockLevel;
            syncStockStatus();
        }
    }

    public void decreaseQuantity(int qty) {
        if (qty > 0 && qty <= stockLevel) {
            this.stockLevel -= qty;
            this.quantity = this.stockLevel;
            this.stockData = "Removed " + qty + " units. Current stock: " + stockLevel;
            syncStockStatus();
        }
    }

    public void setStockLevel(int stockLevel) {
        if (stockLevel < 0) {
            throw new IllegalArgumentException("Stock level cannot be negative.");
        }

        this.stockLevel = stockLevel;
        this.quantity = stockLevel;
        updateStock();
        syncStockStatus();
    }

    public void setMinimumStockLevel(int minimumStockLevel) {
        if (minimumStockLevel < 0) {
            throw new IllegalArgumentException("Minimum stock level cannot be negative.");
        }

        this.reorderThreshold = minimumStockLevel;
        updateStock();
        syncStockStatus();
    }

    public void syncStockStatus() {
        if (stockLevel <= 0) {
            availability = "Not Available";
            stockCondition = "Out of Stock";
            lowStockNotes = "No stock available.";
        } else if (stockLevel <= reorderThreshold) {
            availability = "Available";
            stockCondition = "Low Stock";
            lowStockNotes = "Stock is below or at the reorder threshold.";
        } else {
            availability = "Available";
            stockCondition = "In Stock";
            lowStockNotes = "Stock is within the safe range.";
        }
    }

    public int getInventoryID() {
        return inventoryID;
    }

    public void setInventoryID(int inventoryID) {
        this.inventoryID = inventoryID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getReorderThreshold() {
        return reorderThreshold;
    }

    public int getLowStockThreshold() {
        return reorderThreshold;
    }

    public String getStockData() {
        return stockData;
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }

    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getWarehouseLocation() {
        return warehouseLocation;
    }

    public void setWarehouseLocation(String warehouseLocation) {
        this.warehouseLocation = warehouseLocation;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getStockCondition() {
        return stockCondition;
    }

    public void setStockCondition(String stockCondition) {
        this.stockCondition = stockCondition;
    }

    public String getLowStockNotes() {
        return lowStockNotes;
    }

    public void setLowStockNotes(String lowStockNotes) {
        this.lowStockNotes = lowStockNotes;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
