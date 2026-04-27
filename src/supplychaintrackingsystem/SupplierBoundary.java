/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package supplychaintrackingsystem;

/**
 *
 * @author Rawan
 */
public class SupplierBoundary {
      private ProductionController controller;

    public SupplierBoundary(ProductionController controller) {
        this.controller = controller;
    }

    public boolean supplyRawMaterial(ProductionRecord record, RawMaterial material) {
        return controller.addRawMaterial(record, material);
    }

    public String viewSupplyStatus(RawMaterial material) {
        if (material == null) {
            return "No Material Found";
        }

        if (material.isApproved()) {
            return "Approved";
        } else {
            return "Pending";
        }
    }
    
    
}
