package supplychaintrackingsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductionRecord {
    private int batchID;
    private final List<RawMaterial> materials = new ArrayList<>();
    private Product product;
    private String status;
    
       /////////////////////////////////
  
    //fatma
    private int productID;
    private String productName;
    private double price;
    private int quantity;
    private boolean available;
    
    public String getProductName() {
    return productName;
    }
    public int getQuantity() {
    return quantity;
    }
    
    public int getProductID() {
    return productID;
    }

    public double getPrice() {
    return price;
    }

    public boolean isAvailable() {
    return available && quantity > 0;
} 

    /////////////////////////////////////////////////////////////
    
    

    public ProductionRecord() {
    }

    public ProductionRecord(int batchID) {
        this.batchID = batchID;
    }

    public ProductionRecord(String status) {
        this.status = status;
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

    public String getStatus() {
        return status;
    }
}
