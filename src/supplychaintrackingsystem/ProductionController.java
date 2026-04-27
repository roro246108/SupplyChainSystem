package supplychaintrackingsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductionController {
    private final List<ProductionRecord> productionRecords = new ArrayList<>();
    private Inventory inventory;

    public ProductionController(Inventory inventory) {
        this.inventory = inventory;
    }

    public List<ProductionRecord> getProductionRecords() {
        return productionRecords;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
    
    
    public boolean updateInventory(RawMaterial material) {
    if (material == null || !material.isApproved()) {
        return false;
    }

    inventory.addStock(1);
    return true;
}
    
    public boolean consumeRawMaterial(ProductionRecord record, RawMaterial material) {
    if (record == null || material == null) {
        return false;
    }

    boolean removed = record.removeMaterial(material);

    if (removed) {
        inventory.removeStock(1);
    }

    return removed;
}
    
    
   public boolean addRawMaterial(ProductionRecord record, RawMaterial material) {

    if (record == null || material == null) {
        return false;
    }

    return record.addMaterial(material);
}

    public ProductionRecord createProductionRecord(List<RawMaterial> materials, Product product) {

    if (product == null) {
        throw new IllegalArgumentException("Product is null");
    }

    ProductionRecord record =
            new ProductionRecord(productionRecords.size() + 1);

    record.setProduct(product);

    if (materials != null) {
        for (RawMaterial material : materials) {
            record.addMaterial(material);
        }
    }

    productionRecords.add(record);

    product.addProductionRecord(record);

    return record;
}

  

    public boolean addProductToInventory(Product product) {

    if (product == null) {
        return false;
    }

    product.setStatus("Available");

    return true;
}

    public List<ProductionRecord> viewProductionRecords() {
        return Collections.unmodifiableList(productionRecords);
    }
}
