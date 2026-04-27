/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package supplychaintrackingsystem;

import java.util.List;

/**
 *
 * @author Rawan
 */
public class ManufacturerBoundary {
    private ProductionController controller;

    public ManufacturerBoundary(ProductionController controller) {
        this.controller = controller;
    }

    public ProductionRecord startProduction(List<RawMaterial> materials, Product product) {
        return controller.createProductionRecord(materials, product);
    }

    public List<ProductionRecord> viewProductionRecords() {
        return controller.viewProductionRecords();
    }
}
