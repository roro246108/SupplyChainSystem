package supplychaintrackingsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductionRecord {
    private int batchID;
    private final List<RawMaterial> materials = new ArrayList<>();
    private Product product;
    private String status;
    
     //rawan
    public ProductionRecord() {
        this.status = "NEW";
    }

    //rawan
   public ProductionRecord(int batchID) {
        this();

        if (batchID > 0) {
            this.batchID = batchID;
        }
    }

    public ProductionRecord(String status) {
        this.status = status;
    }

    
    public String getStatus() {
        return status;
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

        if (product == null) {
            throw new IllegalArgumentException("Product is null");
        }

        this.product = product;
    }
     
   
   public boolean assignToProduction(int batchID) {

        if (batchID <= 0) {
            return false;
        }

        if (this.batchID > 0) {
            return false; // already assigned
        }

        this.batchID = batchID;
        this.status = "ASSIGNED";

        return true;
    }
  
  
   public boolean recordBatchInformation(String productionData) {

        if (productionData == null || productionData.trim().isEmpty()) {
            return false;
        }

        if (batchID <= 0) {
            return false;
        }

        this.status = productionData.trim();

        return true;
    }
  
  
public String reviewProductionRecord(String batchID) {

        if (batchID == null || batchID.trim().isEmpty()) {
            return "Invalid batch ID";
        }

        return "Batch ID: " + this.batchID +
               ", Materials: " + materials.size() +
               ", Status: " + status;
    }

   public boolean validateProduction() {

        if (batchID <= 0) {
            return false;
        }

        if (product == null) {
            return false;
        }

        if (materials.isEmpty()) {
            return false;
        }

        for (RawMaterial material : materials) {
            if (material == null || !material.isApproved()) {
                return false;
            }
        }

        return true;
    }

   public boolean addMaterial(RawMaterial material) {

        if (material == null) {
            return false;
        }

        if (!material.isApproved()) {
            return false;
        }

        if (materials.contains(material)) {
            return false;
        }

        materials.add(material);
        return true;
    }
   
   public boolean removeMaterial(RawMaterial material) {

        if (material == null) {
            return false;
        }

        return materials.remove(material);
    }

    // useful extra method
    public void markCompleted() {

        if (validateProduction()) {
            status = "COMPLETED";
        }
    }

    @Override
    public String toString() {
        return "ProductionRecord{" +
                "batchID=" + batchID +
                ", materials=" + materials.size() +
                ", status='" + status + '\'' +
                '}';
    }

   
}
