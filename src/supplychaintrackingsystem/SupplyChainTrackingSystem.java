/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package supplychaintrackingsystem;

/**
 *
 * @author Rawan
 */
public class SupplyChainTrackingSystem {

    public static void main(String[] args) {
           DBConnection.connect();
        
        Customer customer = new Customer(1, "Amina Hassan", "amina@example.com", "secret123",
                "Customer", "12 Nile Street");
        Supplier supplier = new Supplier(2, "Delta Supplier", "supplier@example.com", "secret123",
                "Supplier", 201234567);
        Distributor distributor = new Distributor(3, "North Distributor", "dist@example.com", "secret123",
                "Distributor", "Cairo Warehouse");
        Logistics logistics = new Logistics(4, "Logistics Team", "logistics@example.com", "secret123",
                "Logistics", 4001, "Transport", "Greater Cairo", "01000000000");
        Retailer retailer = new Retailer(5, "Retail One", "retailer@example.com", "secret123",
                "Retailer");
        Manufacturer manufacturer = new Manufacturer(6, "Factory Co", "factory@example.com", "secret123",
                "Manufacturer", "Industrial Zone");
        SystemAdministrator admin = new SystemAdministrator(7, "Admin User", "admin@example.com",
                "secret123", "SystemAdministrator");
        Regulator regulator = new Regulator(8, "Reg Office", "reg@example.com", "secret123",
                "Regulator", 8001, "Trade Authority", "High", "Cairo");

        Shipment shipment = new Shipment(1001);

        customer.addShipment(shipment);
        supplier.addShipment(shipment);
        distributor.addShipment(shipment);
        logistics.addShipment(shipment);
        retailer.addShipment(shipment);
        manufacturer.addShipment(shipment);

        admin.addUser(customer);
        admin.addUser(supplier);
        admin.addUser(distributor);
        admin.addUser(logistics);
        admin.addUser(retailer);
        admin.addUser(manufacturer);
        admin.addSupplier(supplier);

        regulator.addManufacturer(manufacturer);
        retailer.setDistributor(distributor);

        System.out.println(customer.viewProfile());
        System.out.println("Customer shipments: " + customer.viewDeliveryUpdates().size());
        System.out.println("Supplier shipments: " + supplier.getShipments().size());
        System.out.println("Distributor retailers: " + distributor.getRetailers().size());
        System.out.println("Logistics shipments: " + logistics.getShipments().size());
        System.out.println("Admin users: " + admin.getManagedUsers().size());
        System.out.println("Regulator manufacturers: " + regulator.getManufacturers().size());
    }
}
