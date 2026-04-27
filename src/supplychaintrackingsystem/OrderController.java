package supplychaintrackingsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderController {

    private final Map<Integer, Order> orders = new HashMap<>();
    private final Map<Integer, Product> productsCatalog = new HashMap<>();
    private int nextOrderID = 1;

    public void addProductToCatalog(Product product) {
        try {
            if (product == null) {
                throw new NullPointerException("Product cannot be null.");
            }

            productsCatalog.put(product.getProductID(), product);

        } catch (Exception e) {
            System.out.println("Error adding product to catalog: " + e.getMessage());
        }
    }

    public Order createOrder(int userID, List<Product> products) {
        try {
            if (userID <= 0) {
                throw new IllegalArgumentException("Customer ID must be positive.");
            }

            if (products == null || products.isEmpty()) {
                throw new IllegalArgumentException("Products list cannot be empty.");
            }

            for (Product product : products) {
                if (product == null) {
                    throw new NullPointerException("Product cannot be null.");
                }

                if (!checkAvailability(product.getProductID())) {
                    throw new IllegalStateException("Product is not available: " + product.getProductName());
                }
            }

            Order order = new Order(nextOrderID++, userID);

            for (Product product : products) {
                order.addProduct(product);
                updateInventory(product.getProductID());
            }

            order.setStatus("Pending");
            orders.put(order.getOrderID(), order);

            System.out.println("Order created successfully.");
            return order;

        } catch (Exception e) {
            System.out.println("Error creating order: " + e.getMessage());
            return null;
        }
    }

    public List<OrderReadOnly> getOrders(int customerID) {
        List<OrderReadOnly> customerOrders = new ArrayList<>();

        try {
            if (customerID <= 0) {
                throw new IllegalArgumentException("Customer ID must be positive.");
            }

            for (Order order : orders.values()) {
                if (order.getCustomerID() == customerID) {
                    customerOrders.add(order);
                }
            }

        } catch (Exception e) {
            System.out.println("Error getting orders: " + e.getMessage());
        }

        return customerOrders;
    }

    public double calculateTotal(OrderReadOnly order) {
        try {
            if (order == null) {
                throw new NullPointerException("Order cannot be null.");
            }

            if (order instanceof Order fullOrder) {
                return fullOrder.getTotalAmount();
            }

            throw new IllegalArgumentException("Order total is not available for this order view.");

        } catch (Exception e) {
            System.out.println("Error calculating total: " + e.getMessage());
            return 0;
        }
    }

    public boolean checkAvailability(int productID) {
        try {
            if (productID <= 0) {
                throw new IllegalArgumentException("Product ID must be positive.");
            }

            Product product = productsCatalog.get(productID);

            if (product == null) {
                throw new IllegalArgumentException("Product not found.");
            }

            return product.checkAvailability();

        } catch (Exception e) {
            System.out.println("Error checking availability: " + e.getMessage());
            return false;
        }
    }

    public void updateInventory(int productID) {
        try {
            if (productID <= 0) {
                throw new IllegalArgumentException("Product ID must be positive.");
            }

            Product product = productsCatalog.get(productID);

            if (product == null) {
                throw new IllegalArgumentException("Product not found.");
            }

            Inventory inventory = product.getInventory();

            if (inventory == null) {
                throw new IllegalStateException("Product has no inventory linked.");
            }

            inventory.removeStock(1);

            System.out.println("Inventory updated for product ID: " + productID);

        } catch (Exception e) {
            System.out.println("Error updating inventory: " + e.getMessage());
        }
    }

    public Order getOrderByID(int orderID) {
        try {
            return getOrder(orderID);

        } catch (Exception e) {
            System.out.println("Error finding order: " + e.getMessage());
            return null;
        }
    }

    private Order getOrder(int orderID) {
        Order order = orders.get(orderID);

        if (order == null) {
            throw new IllegalArgumentException("Order not found.");
        }

        return order;
    }
}
