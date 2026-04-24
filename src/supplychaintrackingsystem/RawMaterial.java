package supplychaintrackingsystem;

public class RawMaterial {
    private String materialID;
    private String materialData;
    private boolean approved;

    public RawMaterial() {
    }

    public RawMaterial(String materialID, String materialData) {
        this.materialID = materialID;
        this.materialData = materialData;
    }

    public void registerRawMaterial(String materialData) {
        this.materialData = materialData;
    }

    public boolean approveMaterial(int adminID) {
        this.approved = adminID > 0;
        return approved;
    }

    public void linkRawMaterial(String materialID) {
        this.materialID = materialID;
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
}
