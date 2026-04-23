package supplychaintrackingsystem;

public class OutOfStockState implements InventoryState {
    @Override
    public void addStock(Inventory inventory, int quantity) {
        inventory.increaseQuantity(quantity);
        if (inventory.getQuantity() > inventory.getLowStockThreshold()) {
            inventory.setState(new InStockState());
        } else {
            inventory.setState(new LowStockState());
        }
    }

    @Override
    public void updateStock(Inventory inventory) {
        // No-op.
    }

    @Override
    public boolean checkAvailability(Inventory inventory) {
        return false;
    }

    @Override
    public void detectLowStock(Inventory inventory) {
        // Already out of stock.
    }
}
