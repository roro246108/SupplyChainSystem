package supplychaintrackingsystem;

public class RawMaterial {
    private String materialID;
    private String materialData;
    private boolean approved;

    public RawMaterial() {
    }

public RawMaterial(String materialID, String materialData) {
        this();
        this.materialID = materialID;
        this.materialData = materialData;
    }

    public String getMaterialID() {
        return materialID;
    }

    public String getMaterialData() {
        return materialData;
    }

    public boolean isApproved() {
        return approved;
    }

    
    public boolean registerRawMaterial(String materialData) {

    if (materialData == null || materialData.trim().isEmpty()) {
        return false;
    }

    
    if (this.materialData != null &&
        this.materialData.equalsIgnoreCase(materialData.trim())) {
        return false;
    }

    this.materialData = materialData.trim();

    
    this.approved = false;

    return true;
}
   
    public boolean approveMaterial(int adminID) {

    if (adminID <= 0) return false;

    if (materialID == null || materialData == null) {
        return false;
    }

    if (approved) {
        return false; // already approved
    }

    approved = true;
    return true;
}
    

   
    public boolean linkRawMaterial(String materialID) {

        if (materialID == null || materialID.trim().isEmpty()) {
            return false;
        }

        this.materialID = materialID.trim();
        return true;
    }

   
    public boolean updateMaterialData(String newData) {

        if (newData == null || newData.trim().isEmpty()) {
            return false;
        }

        this.materialData = newData.trim();
        this.approved = false; // updated data requires new approval

        return true;
    }

   
    public void revokeApproval() {
        approved = false;
    }

    
   public boolean revokeApproval(String reason) {

    if (!approved) {
        return false;
    }

    if (reason == null || reason.trim().isEmpty()) {
        return false; 
    }
    approved = false;
    return true;
}
    
    
    
    @Override
    public String toString() {
        return "RawMaterial{" +
                "materialID='" + materialID + '\'' +
                ", materialData='" + materialData + '\'' +
                ", approved=" + approved +
                '}';
    }
}

