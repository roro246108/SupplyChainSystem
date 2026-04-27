package supplychaintrackingsystem;

public class InventoryBoundary {
    private final InventoryController controller;

    public InventoryBoundary(InventoryController controller) {
        this.controller = controller;
    }

    public Inventory addStock(int inventoryID, int productID, String productName, String warehouseLocation,
                              Integer currentQuantity, Integer addQuantity, Integer minimumStockLevel) {
        return controller.addStock(inventoryID, productID, productName, warehouseLocation,
                currentQuantity, addQuantity, minimumStockLevel);
    }

    public Inventory updateStock(int inventoryID, int productID, String productName, String warehouseLocation,
                                 Integer currentQuantity, Integer minimumStockLevel) {
        return controller.updateStock(inventoryID, productID, productName, warehouseLocation,
                currentQuantity, minimumStockLevel);
    }

    public Inventory checkAvailability(int inventoryID, int productID, String productName, String warehouseLocation,
                                       Integer currentQuantity, Integer minimumStockLevel) {
        return controller.checkAvailability(inventoryID, productID, productName, warehouseLocation,
                currentQuantity, minimumStockLevel);
    }

    public Inventory detectLowStock(int inventoryID, int productID, String productName, String warehouseLocation,
                                    Integer currentQuantity, Integer minimumStockLevel) {
        return controller.detectLowStock(inventoryID, productID, productName, warehouseLocation,
                currentQuantity, minimumStockLevel);
    }

    public void clearInventory(int inventoryID) {
        controller.clearInventory(inventoryID);
    }
}
