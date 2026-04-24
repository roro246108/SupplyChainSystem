package supplychaintrackingsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Production {
    private final List<ProductionRecord> productionRecords = new ArrayList<>();

    public void addRawMaterial(int supplierID, RawMaterial material) {
        if (material != null) {
            productionRecords.add(new ProductionRecord(supplierID));
        }
    }

    public ProductionRecord createProductionRecord(List<RawMaterial> materials, Product product) {
        ProductionRecord record = new ProductionRecord(productionRecords.size() + 1);
        if (materials != null) {
            materials.forEach(record::addMaterial);
        }
        record.setProduct(product);
        productionRecords.add(record);
        return record;
    }

    public void updateInventory(RawMaterial material) {
        System.out.println("Inventory updated for material: " + (material == null ? "null" : material.getMaterialID()));
    }

    public void consumeRawMaterial(RawMaterial material) {
        System.out.println("Consumed raw material: " + (material == null ? "null" : material.getMaterialID()));
    }

    public void addProductToInventory(Product product) {
        System.out.println("Added product to inventory: " + (product == null ? "null" : product.getProductName()));
    }

    public List<ProductionRecord> viewProductionRecords() {
        return Collections.unmodifiableList(productionRecords);
    }
}
