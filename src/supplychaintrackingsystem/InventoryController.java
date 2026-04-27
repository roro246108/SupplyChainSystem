package supplychaintrackingsystem;

import java.util.HashMap;
import java.util.Map;

public class InventoryController {
    private final Map<Integer, Inventory> inventories = new HashMap<>();

    public Inventory getInventory(int inventoryID) {
        return inventories.get(inventoryID);
    }

    public Inventory addStock(int inventoryID, int productID, String productName, String warehouseLocation,
                              Integer currentQuantity, Integer addQuantity, Integer minimumStockLevel) {
        validateBaseFields(inventoryID, productID, productName, warehouseLocation);

        if (addQuantity == null || addQuantity <= 0) {
            throw new IllegalArgumentException("Add Quantity must be a positive number.");
        }

        Inventory inventory = inventories.computeIfAbsent(inventoryID, id -> new Inventory());
        int baseQuantity = currentQuantity == null ? inventory.getStockLevel() : currentQuantity;

        applyMetadata(inventory, inventoryID, productID, productName, warehouseLocation, minimumStockLevel);
        inventory.setStockLevel(baseQuantity + addQuantity);
        stampInventory(inventory);
        inventories.put(inventoryID, inventory);
        return inventory;
    }

    public Inventory updateStock(int inventoryID, int productID, String productName, String warehouseLocation,
                                 Integer currentQuantity, Integer minimumStockLevel) {
        validateBaseFields(inventoryID, productID, productName, warehouseLocation);

        if (currentQuantity == null || currentQuantity < 0) {
            throw new IllegalArgumentException("Current Quantity must be zero or a positive number.");
        }

        Inventory inventory = inventories.computeIfAbsent(inventoryID, id -> new Inventory());
        applyMetadata(inventory, inventoryID, productID, productName, warehouseLocation, minimumStockLevel);
        inventory.setStockLevel(currentQuantity);
        stampInventory(inventory);
        inventories.put(inventoryID, inventory);
        return inventory;
    }

    public Inventory checkAvailability(int inventoryID, int productID, String productName, String warehouseLocation,
                                       Integer currentQuantity, Integer minimumStockLevel) {
        validateBaseFields(inventoryID, productID, productName, warehouseLocation);

        Inventory inventory = inventories.computeIfAbsent(inventoryID, id -> new Inventory());
        applyMetadata(inventory, inventoryID, productID, productName, warehouseLocation, minimumStockLevel);
        if (currentQuantity != null && currentQuantity >= 0) {
            inventory.setStockLevel(currentQuantity);
        }
        stampInventory(inventory);
        inventories.put(inventoryID, inventory);
        return inventory;
    }

    public Inventory detectLowStock(int inventoryID, int productID, String productName, String warehouseLocation,
                                    Integer currentQuantity, Integer minimumStockLevel) {
        Inventory inventory = checkAvailability(inventoryID, productID, productName, warehouseLocation,
                currentQuantity, minimumStockLevel);
        if (inventory.isBelowThreshold()) {
            inventory.setLowStockNotes("Low stock alert: inventory is below reorder threshold.");
            inventory.generateLowStockAlert();
        } else {
            inventory.setLowStockNotes("Stock is within the safe range.");
        }
        return inventory;
    }

    public void clearInventory(int inventoryID) {
        inventories.remove(inventoryID);
    }

    private void applyMetadata(Inventory inventory, int inventoryID, int productID, String productName,
                               String warehouseLocation, Integer minimumStockLevel) {
        inventory.setInventoryID(inventoryID);
        inventory.setProductID(productID);
        inventory.setProductName(productName.trim());
        inventory.setWarehouseLocation(warehouseLocation.trim());

        Product product = inventory.getProduct();
        if (product == null) {
            product = new Product();
            inventory.setProduct(product);
        }
        product.setProductName(productName.trim());
        product.setInventory(inventory);

        if (minimumStockLevel != null && minimumStockLevel >= 0) {
            inventory.setMinimumStockLevel(minimumStockLevel);
        }
        inventory.setAvailability(inventory.checkAvailability() ? "Available" : "Not Available");
        inventory.setStockCondition(inventory.isBelowThreshold() && inventory.getStockLevel() > 0
                ? "Low Stock"
                : (inventory.getStockLevel() == 0 ? "Out of Stock" : "In Stock"));
    }

    private void stampInventory(Inventory inventory) {
        inventory.setLastUpdated(java.time.LocalDateTime.now().toString());
    }

    private void validateBaseFields(int inventoryID, int productID, String productName, String warehouseLocation) {
        if (inventoryID <= 0) {
            throw new IllegalArgumentException("Inventory ID must be a positive number.");
        }
        if (productID <= 0) {
            throw new IllegalArgumentException("Product ID must be a positive number.");
        }
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Product Name cannot be empty.");
        }
        if (warehouseLocation == null || warehouseLocation.isBlank()) {
            throw new IllegalArgumentException("Warehouse Location cannot be empty.");
        }
    }
}
