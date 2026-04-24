package supplychaintrackingsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Inventory {
    private InventoryState currentState;
    private final List<Product> products = new ArrayList<>();
    private final List<RawMaterial> rawMaterials = new ArrayList<>();
    private int quantity;
    private int lowStockThreshold = 5;

    public Inventory() {
        this.currentState = new OutOfStockState();
    }

    public void setState(InventoryState state) {
        this.currentState = state;
    }

    public InventoryState getCurrentState() {
        return currentState;
    }

    public void addStock(int qty) {
        currentState.addStock(this, qty);
    }

    public void updateStock() {
        currentState.updateStock(this);
    }

    public boolean checkAvailability() {
        return currentState.checkAvailability(this);
    }

    public void detectLowStock() {
        currentState.detectLowStock(this);
    }

    public void increaseQuantity(int qty) {
        this.quantity += Math.max(0, qty);
    }

    public int getQuantity() {
        return quantity;
    }

    public int getLowStockThreshold() {
        return lowStockThreshold;
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public List<RawMaterial> getRawMaterials() {
        return Collections.unmodifiableList(rawMaterials);
    }

    public void addProductToInventory(Product product) {
        if (product != null) {
            products.add(product);
        }
    }

    public void addRawMaterial(RawMaterial material) {
        if (material != null) {
            rawMaterials.add(material);
        }
    }

    public void consumeRawMaterial(RawMaterial material) {
        rawMaterials.remove(material);
    }
}
