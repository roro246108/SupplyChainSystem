package supplychaintrackingsystem;

public interface InventoryState {
    void addStock(Inventory inventory, int quantity);

    void updateStock(Inventory inventory);

    boolean checkAvailability(Inventory inventory);

    void detectLowStock(Inventory inventory);
}
