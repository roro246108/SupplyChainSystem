package supplychaintrackingsystem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {

    private int orderID;
    private String status;
   
    private LocalDate orderDate;
    private double totalAmount;
    private LocalDate estimatedDelivery;

    private final List<Product> products = new ArrayList<>();
    private Shipment shipment;

    private String cancellationReason;
    private boolean paid;
    private boolean refunded;

    private static final String PENDING = "Pending";
    private static final String CONFIRMED = "Confirmed";
    private static final String PROCESSING = "Processing";
    private static final String SHIPPED = "Shipped";
    private static final String DELIVERED = "Delivered";
    private static final String CANCELLED = "Cancelled";
    private static final String RETURNED = "Returned";
    private static final String REFUNDED = "Refunded";

    private static final List<String> VALID_STATUSES = List.of(
            PENDING,
            CONFIRMED,
            PROCESSING,
            SHIPPED,
            DELIVERED,
            CANCELLED,
            RETURNED,
            REFUNDED
    );

    public Order(int orderID, Customer customer, List<Product> products) {
        if (orderID <= 0) {
            throw new IllegalArgumentException("Order ID must be positive.");
        }

        if (customerID <= 0) {
            throw new IllegalArgumentException("Customer ID must be positive.");
        }

        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one product.");
        }

        this.orderID = orderID;
        this.customer = customer;
        this.orderDate = LocalDate.now();
        this.status = PENDING;
        this.estimatedDelivery = orderDate.plusDays(5);
        this.paid = false;
        this.refunded = false;

        for (Product product : products) {
            addProduct(product);
        }

        this.totalAmount = calculateTotal();
   
    private String trackingNumber;//rawan
    private OrderType orderType;
   private Customer customer;// rawan
 
    

    public Order() {
        this.orderDate = new Date();
        this.status = "NEW";
        this.orderType = OrderType.STANDARD;
    }
      
   
         //rawan
    public Order(int orderID, int quantity, Date orderDate, String status, Date estimatedDelivery, String trackingNumber, OrderType orderType, Customer customer, Shipment shipment) {
        this.orderID = orderID;
        this.quantity = quantity;
        this.orderDate = orderDate;
        this.status = status;
        this.estimatedDelivery = estimatedDelivery;
        this.trackingNumber = trackingNumber;
        this.orderType = orderType;
        this.customer = customer;
        this.shipment = shipment;
    }
   
    
   
    }

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }

        if (!canModifyProducts()) {
            throw new IllegalStateException("Products cannot be modified when order status is: " + status);
        }

        if (!product.isAvailable()) {
            throw new IllegalStateException("Product is not available.");
        }

        products.add(product);
        totalAmount = calculateTotal();
    }

    public void removeProduct(int productID) {
        if (productID <= 0) {
            throw new IllegalArgumentException("Product ID must be positive.");
        }

        if (!canModifyProducts()) {
            throw new IllegalStateException("Products cannot be removed when order status is: " + status);
        }

        boolean removed = products.removeIf(product -> product.getProductID() == productID);

        if (!removed) {
            throw new IllegalArgumentException("Product not found in this order.");
        }

        if (products.isEmpty()) {
            throw new IllegalStateException("Order cannot be empty after removing product.");
        }

        totalAmount = calculateTotal();
    }

    public double calculateTotal() {
        double total = 0;

        for (Product product : products) {
            if (product == null) {
                throw new IllegalStateException("Order contains invalid product.");
            }

            if (product.getPrice() < 0) {
                throw new IllegalStateException("Product price cannot be negative.");
            }

            total += product.getPrice();
        }

        return total;
    }

    public boolean checkAvailability(int productID) {
        if (productID <= 0) {
            throw new IllegalArgumentException("Product ID must be positive.");
        }

        for (Product product : products) {
            if (product.getProductID() == productID) {
                return product.isAvailable();
            }
        }

        return false;
    }

    public boolean validateOrder() {
        if (orderID <= 0 || customerID <= 0) {
            return false;
        }

        if (products.isEmpty()) {
            return false;
        }

        if (!VALID_STATUSES.contains(status)) {
            return false;
        }

        for (Product product : products) {
            if (product == null) {
                return false;
            }

            if (product.getPrice() < 0) {
                return false;
            }

            if (!product.isAvailable()
                    && status.equals(PENDING)) {
                return false;
            }
        }

        return calculateTotal() > 0;
    }

    public void confirmOrder() {
        if (!status.equals(PENDING)) {
            throw new IllegalStateException("Only pending orders can be confirmed.");
        }

        if (!validateOrder()) {
            throw new IllegalStateException("Order cannot be confirmed because it is invalid.");
        }

        if (!allProductsAvailable()) {
            throw new IllegalStateException("Order cannot be confirmed because one or more products are unavailable.");
        }

        status = CONFIRMED;
        totalAmount = calculateTotal();
        estimatedDelivery = orderDate.plusDays(5);
    }

    public void markAsPaid() {
        if (!status.equals(CONFIRMED) && !status.equals(PROCESSING)) {
            throw new IllegalStateException("Only confirmed or processing orders can be paid.");
        }

        if (paid) {
            throw new IllegalStateException("Order is already paid.");
        }

        paid = true;
    }

    public void processOrder() {
        if (!status.equals(CONFIRMED)) {
            throw new IllegalStateException("Only confirmed orders can be processed.");
        }

        if (!paid) {
            throw new IllegalStateException("Order must be paid before processing.");
        }

        status = PROCESSING;
    }

    public void shipOrder(Shipment shipment) {
        if (shipment == null) {
            throw new IllegalArgumentException("Shipment cannot be null.");
        }

        if (!status.equals(PROCESSING)) {
            throw new IllegalStateException("Only processing orders can be shipped.");
        }

        this.shipment = shipment;
        status = SHIPPED;
        estimatedDelivery = LocalDate.now().plusDays(3);
    }

    public void markAsDelivered() {
        if (!status.equals(SHIPPED)) {
            throw new IllegalStateException("Only shipped orders can be delivered.");
        }

        status = DELIVERED;
        estimatedDelivery = LocalDate.now();
    }

    public void cancelOrder(String reason) {
        if (status.equals(SHIPPED) || status.equals(DELIVERED)) {
            throw new IllegalStateException("Cannot cancel shipped or delivered orders.");
        }

        if (status.equals(CANCELLED)) {
            throw new IllegalStateException("Order is already cancelled.");
        }

        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Cancellation reason is required.");
        }

        status = CANCELLED;
        cancellationReason = reason.trim();

        if (paid) {
            refundOrder();
        }
    }

    public void returnOrder(String reason) {
        if (!status.equals(DELIVERED)) {
            throw new IllegalStateException("Only delivered orders can be returned.");
        }

        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Return reason is required.");
        }

        status = RETURNED;

        if (paid) {
            refundOrder();
        }
    }

    public void refundOrder() {
        if (!paid) {
            throw new IllegalStateException("Cannot refund unpaid order.");
        }

        if (refunded) {
            throw new IllegalStateException("Order is already refunded.");
        }

        if (!status.equals(CANCELLED) && !status.equals(RETURNED)) {
            throw new IllegalStateException("Only cancelled or returned orders can be refunded.");
        }

        refunded = true;
        status = REFUNDED;
    }

    public void updateStatus(String newStatus) {
        if (newStatus == null || newStatus.isBlank()) {
            throw new IllegalArgumentException("Status cannot be empty.");
        }

        if (!VALID_STATUSES.contains(newStatus)) {
            throw new IllegalArgumentException("Invalid order status: " + newStatus);
        }

        if (status.equals(DELIVERED) && !newStatus.equals(RETURNED)) {
            throw new IllegalStateException("Delivered order can only move to Returned.");
        }

        if (status.equals(CANCELLED) || status.equals(REFUNDED)) {
            throw new IllegalStateException("Final order status cannot be changed.");
        }

        status = newStatus;
    }

    public String trackOrder() {
        if (shipment == null) {
            return "Order " + orderID + " status: " + status + ". No shipment assigned yet.";
        }

        return "Order " + orderID + " status: " + status + ". Shipment details: " + shipment;
    }

    private boolean allProductsAvailable() {
        for (Product product : products) {
            if (!product.isAvailable()) {
                return false;
            }
        }
        return true;
    }

    private boolean canModifyProducts() {
        return status.equals(PENDING) || status.equals(CONFIRMED);
    }

    public int getOrderID() {
        return orderID;
    }

    public String getStatus() {
        return status;
    }

    public int getCustomerID() {
        return customerID;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public LocalDate getEstimatedDelivery() {
        return estimatedDelivery;
    }

    public boolean isPaid() {
        return paid;
    }

    public boolean isRefunded() {
        return refunded;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public Shipment getShipment() {
        return shipment;
    }
    
    //assign shipement rawan
    public void assignShipment(Shipment shipment) {
    if (shipment == null) {
        throw new IllegalArgumentException("Shipment is null");
    }
    this.shipment = shipment;
}
    // track order rawan 
    public String trackOrder() {
    if (shipment == null) {
        return "Order not shipped yet";
    }
    return shipment.trackShipment(shipment.getShipmentID());
}


    @Override
    public String toString() {
        return "Order{"
                + "orderID=" + orderID
                ", quantity=" + quantity +
                + ", status='" + status + '\''
                + ", customerID=" + customer
                + ", orderDate=" + orderDate
                + ", totalAmount=" + totalAmount
                + ", estimatedDelivery=" + estimatedDelivery
                + ", paid=" + paid
                + ", refunded=" + refunded
                + ", products=" + products.size()
                 ", trackingNumber='" + trackingNumber + '\'' +
                ", orderType=" + orderType +
                + '}';
    }
}
