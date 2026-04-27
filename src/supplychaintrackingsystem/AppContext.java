package supplychaintrackingsystem;

import javax.swing.JFrame;

public final class AppContext {
    private static UserController USER_CONTROLLER;
    private static AuthBoundary AUTH_BOUNDARY;
    private static OrderController ORDER_CONTROLLER;
    private static OrderBoundary ORDER_BOUNDARY;
    private static InventoryController INVENTORY_CONTROLLER;
    private static InventoryBoundary INVENTORY_BOUNDARY;
    private static ShipmentController SHIPMENT_CONTROLLER;
    private static ShipmentBoundary SHIPMENT_BOUNDARY;
    private static LogisticsController LOGISTICS_CONTROLLER;
    private static LogisticsBoundary LOGISTICS_BOUNDARY;
    private static User currentUser;

    private AppContext() {
    }

    public static synchronized AuthBoundary authBoundary() {
        if (AUTH_BOUNDARY == null) {
            AUTH_BOUNDARY = new AuthBoundary(userController());
        }
        return AUTH_BOUNDARY;
    }

    public static synchronized UserController userController() {
        if (USER_CONTROLLER == null) {
            USER_CONTROLLER = new UserController();
        }
        return USER_CONTROLLER;
    }

    public static synchronized InventoryBoundary inventoryBoundary() {
        if (INVENTORY_BOUNDARY == null) {
            INVENTORY_BOUNDARY = new InventoryBoundary(inventoryController());
        }
        return INVENTORY_BOUNDARY;
    }

    public static synchronized InventoryController inventoryController() {
        if (INVENTORY_CONTROLLER == null) {
            INVENTORY_CONTROLLER = new InventoryController();
        }
        return INVENTORY_CONTROLLER;
    }

    public static synchronized OrderBoundary orderBoundary() {
        if (ORDER_BOUNDARY == null) {
            ORDER_BOUNDARY = new OrderBoundary(orderController());
        }
        return ORDER_BOUNDARY;
    }

    public static synchronized OrderController orderController() {
        if (ORDER_CONTROLLER == null) {
            ORDER_CONTROLLER = new OrderController();
        }
        return ORDER_CONTROLLER;
    }

    public static synchronized ShipmentBoundary shipmentBoundary() {
        if (SHIPMENT_BOUNDARY == null) {
            SHIPMENT_BOUNDARY = new ShipmentBoundary(shipmentController());
        }
        return SHIPMENT_BOUNDARY;
    }

    public static synchronized ShipmentController shipmentController() {
        if (SHIPMENT_CONTROLLER == null) {
            SHIPMENT_CONTROLLER = new ShipmentController();
        }
        return SHIPMENT_CONTROLLER;
    }

    public static synchronized LogisticsBoundary logisticsBoundary() {
        if (LOGISTICS_BOUNDARY == null) {
            LOGISTICS_BOUNDARY = new LogisticsBoundary(logisticsController());
        }
        return LOGISTICS_BOUNDARY;
    }

    public static synchronized LogisticsController logisticsController() {
        if (LOGISTICS_CONTROLLER == null) {
            LOGISTICS_CONTROLLER = new LogisticsController();
        }
        return LOGISTICS_CONTROLLER;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static JFrame createDashboardForRole(String role) {
        if (role == null) {
            return null;
        }

        String normalized = normalizeRole(role);
        switch (normalized) {
            case "customer":
                return new CustomerGUI();
            case "supplier":
                return new SupplierGUI();
            case "retailer":
                return new RetailerGUI();
            case "distributor":
                return new DistributorGUI();
            case "regulator":
                return new RegulatorGUI();
            case "manufacturer":
                return new ManufactureGuiii();
            case "logistics":
                return new LogisticsUI();
            case "systemadministrator":
            case "administrator":
                return new SystemAdmintratorGuii();
            default:
                return null;
        }
    }

    public static String[] supportedRoles() {
        return new String[] {
            "Customer",
            "Supplier",
            "Retailer",
            "Distributor",
            "Manufacturer",
            "Logistics",
            "Regulator",
            "System Administrator"
        };
    }

    public static String normalizeRole(String role) {
        if (role == null) {
            return "";
        }

        return role.replaceAll("\\s+", "").trim().toLowerCase();
    }
}
