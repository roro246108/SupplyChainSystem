package supplychaintrackingsystem;

public class LowStockState implements InventoryState {
    @Override
    public void addStock(Inventory inventory, int quantity) {
        if (quantity <= 0) {
            System.out.println("Quantity must be positive.");
            return;
        }

        inventory.increaseQuantity(quantity);
        updateStock(inventory);
        System.out.println("Stock added while inventory was low.");
    }

    @Override
    public void updateStock(Inventory inventory) {
        if (inventory.getStockLevel() == 0) {
            inventory.setState(new OutOfStockState());
        } else if (inventory.getStockLevel() <= inventory.getReorderThreshold()) {
            inventory.setState(new LowStockState());
        } else {
            inventory.setState(new InStockState());
        }
    }

    @Override
    public boolean checkAvailability(Inventory inventory) {
        return true;
    }

    @Override
    public void detectLowStock(Inventory inventory) {
        inventory.generateLowStockAlert();
    }
}
