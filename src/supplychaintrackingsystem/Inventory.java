package supplychaintrackingsystem;

public class Inventory {
    private int inventoryID;
    private int stockLevel;
    private int quantity;
    private String stockData;
    private int reorderThreshold;
    private InventoryState currentState;
    private NotificationService notificationService;
    private Product product;

    public Inventory() {
        this.stockLevel = 0;
        this.quantity = 0;
        this.reorderThreshold = 10;
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
        }
    }

    public void decreaseQuantity(int qty) {
        if (qty > 0 && qty <= stockLevel) {
            this.stockLevel -= qty;
            this.quantity = this.stockLevel;
            this.stockData = "Removed " + qty + " units. Current stock: " + stockLevel;
        }
    }

    public int getInventoryID() {
        return inventoryID;
    }

    public void setInventoryID(int inventoryID) {
        this.inventoryID = inventoryID;
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
}
