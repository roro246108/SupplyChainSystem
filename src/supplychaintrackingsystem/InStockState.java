package supplychaintrackingsystem;

public class InStockState implements InventoryState {
    @Override
    public void addStock(Inventory inventory, int quantity) {
        inventory.increaseQuantity(quantity);
    }

    @Override
    public void updateStock(Inventory inventory) {
        // No-op for this simple model.
    }

    @Override
    public boolean checkAvailability(Inventory inventory) {
        return inventory.getQuantity() > 0;
    }

    @Override
    public void detectLowStock(Inventory inventory) {
        if (inventory.getQuantity() <= inventory.getLowStockThreshold()) {
            inventory.setState(new LowStockState());
        }
    }
}
