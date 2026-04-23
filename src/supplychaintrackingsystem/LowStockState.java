package supplychaintrackingsystem;

public class LowStockState implements InventoryState {
    @Override
    public void addStock(Inventory inventory, int quantity) {
        inventory.increaseQuantity(quantity);
        if (inventory.getQuantity() > inventory.getLowStockThreshold()) {
            inventory.setState(new InStockState());
        }
    }

    @Override
    public void updateStock(Inventory inventory) {
        // No-op.
    }

    @Override
    public boolean checkAvailability(Inventory inventory) {
        return inventory.getQuantity() > 0;
    }

    @Override
    public void detectLowStock(Inventory inventory) {
        if (inventory.getQuantity() <= 0) {
            inventory.setState(new OutOfStockState());
        }
    }
}
