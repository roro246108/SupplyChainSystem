package supplychaintrackingsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductionRecord {
    private int batchID;
    private final List<RawMaterial> materials = new ArrayList<>();
    private Product product;
    private String status;

    public ProductionRecord() {
    }

    public ProductionRecord(int batchID) {
        this.batchID = batchID;
    }

    public void assignToProduction(int batchID) {
        this.batchID = batchID;
    }

    public void recordBatchInformation(String productionData) {
        this.status = productionData;
    }

    public void reviewProductionRecord(String batchID) {
        System.out.println("Review production record " + batchID);
    }

    public boolean validateProduction() {
        return batchID > 0;
    }

    public int getBatchID() {
        return batchID;
    }

    public List<RawMaterial> getMaterials() {
        return Collections.unmodifiableList(materials);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void addMaterial(RawMaterial material) {
        if (material != null) {
            materials.add(material);
        }
    }
}
