package supplychaintrackingsystem;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Order implements OrderReadOnly {

    private int orderID;
    private String status;
    private int customerID;
    private Customer customer;
    private LocalDate orderDate;
    private double totalAmount;
    private LocalDate estimatedDelivery;
    private String trackingNumber;
    private OrderType orderType;

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

    public Order() {
        this.orderDate = LocalDate.now();
        this.estimatedDelivery = orderDate.plusDays(5);
        this.status = PENDING;
        this.orderType = OrderType.STANDARD;
        this.paid = false;
        this.refunded = false;
    }

    public Order(int orderID, int customerID) {
        validateOrderID(orderID);

        if (customerID <= 0) {
            throw new IllegalArgumentException("Customer ID must be positive.");
        }

        this.orderID = orderID;
        this.customerID = customerID;
        this.orderDate = LocalDate.now();
        this.estimatedDelivery = orderDate.plusDays(5);
        this.status = PENDING;
        this.orderType = OrderType.STANDARD;
        this.paid = false;
        this.refunded = false;
        this.totalAmount = 0;
    }

    public Order(int orderID, int customerID, List<Product> products) {
        validateOrderID(orderID);

        if (customerID <= 0) {
            throw new IllegalArgumentException("Customer ID must be positive.");
        }

        this.orderID = orderID;
        this.customerID = customerID;
        initializeOrder(products);
    }

    public Order(int orderID, Customer customer, List<Product> products) {
        validateOrderID(orderID);

        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }

        this.orderID = orderID;
        this.customer = customer;
        this.customerID = customer.getUserID();
        initializeOrder(products);
    }

    public Order(int orderID, int quantity, Date orderDate, String status, Date estimatedDelivery,
                 String trackingNumber, OrderType orderType, Customer customer, Shipment shipment) {
        validateOrderID(orderID);

        this.orderID = orderID;
        this.customer = customer;
        this.customerID = customer == null ? 0 : customer.getUserID();
        this.orderDate = toLocalDate(orderDate == null ? new Date() : orderDate);
        this.status = normalizeStatus(status);
        this.estimatedDelivery = estimatedDelivery == null
                ? this.orderDate.plusDays(5)
                : toLocalDate(estimatedDelivery);
        this.trackingNumber = trackingNumber;
        this.orderType = orderType == null ? OrderType.STANDARD : orderType;
        this.shipment = shipment;
        this.paid = false;
        this.refunded = false;
        this.totalAmount = calculateTotal();
    }

    private void initializeOrder(List<Product> products) {
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one product.");
        }

        this.orderDate = LocalDate.now();
        this.status = PENDING;
        this.estimatedDelivery = orderDate.plusDays(5);
        this.orderType = OrderType.STANDARD;
        this.paid = false;
        this.refunded = false;

        for (Product product : products) {
            addProduct(product);
        }

        this.totalAmount = calculateTotal();
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

    public void setProducts(List<Product> products) {
        if (!canModifyProducts()) {
            throw new IllegalStateException("Products cannot be modified when order status is: " + status);
        }

        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one product.");
        }

        this.products.clear();
        for (Product product : products) {
            if (product == null) {
                throw new IllegalArgumentException("Product cannot be null.");
            }
            if (!product.isAvailable()) {
                throw new IllegalStateException("Product is not available.");
            }
            this.products.add(product);
        }

        totalAmount = calculateTotal();
    }

    public double calculateTotal() {
        double total = 0;

        for (Product product : products) {
            if (product == null) {
                throw new IllegalStateException("Order contains invalid product.");
            }

            double price = product.getBasePrice();
            if (price < 0) {
                throw new IllegalStateException("Product price cannot be negative.");
            }

            total += price;
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

            if (product.getBasePrice() < 0) {
                return false;
            }

            if (!product.isAvailable() && status.equals(PENDING)) {
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

        assignShipment(shipment);
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

    public boolean cancelOrder() {
        cancelOrder("Cancelled by user.");
        return true;
    }

    public void returnOrder(String reason) {
        if (!status.equals(DELIVERED)) {
            throw new IllegalStateException("Only delivered orders can be returned.");
        }

        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Return reason is required.");
        }

        status = RETURNED;
        cancellationReason = reason.trim();

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

        String normalizedStatus = normalizeStatus(newStatus);
        if (!VALID_STATUSES.contains(normalizedStatus)) {
            throw new IllegalArgumentException("Invalid order status: " + newStatus);
        }

        if (status.equals(DELIVERED) && !normalizedStatus.equals(RETURNED)) {
            throw new IllegalStateException("Delivered order can only move to Returned.");
        }

        if (status.equals(CANCELLED) || status.equals(REFUNDED)) {
            throw new IllegalStateException("Final order status cannot be changed.");
        }

        status = normalizedStatus;
    }

    public void updateOrderStatus(String status) {
        updateStatus(status);
    }

    public void setStatus(String status) {
        updateStatus(status);
    }

    public void assignShipment(Shipment shipment) {
        if (shipment == null) {
            throw new IllegalArgumentException("Shipment is null");
        }

        this.shipment = shipment;
        if (trackingNumber == null || trackingNumber.isBlank()) {
            trackingNumber = "SHP-" + shipment.getShipmentID();
        }
    }

    public Invoice generateInvoice() {
        return new Invoice(this);
    }

    public String trackOrder() {
        if (shipment == null) {
            if (trackingNumber == null || trackingNumber.isBlank()) {
                return "Order " + orderID + " status: " + status + ". No shipment assigned yet.";
            }
            return "Order " + orderID + " status: " + status + ". Tracking number: " + trackingNumber;
        }

        return shipment.trackShipment(shipment.getShipmentID());
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

    private void validateOrderID(int orderID) {
        if (orderID <= 0) {
            throw new IllegalArgumentException("Order ID must be positive.");
        }
    }

    private String normalizeStatus(String rawStatus) {
        String trimmed = rawStatus.trim();

        if ("NEW".equalsIgnoreCase(trimmed)) {
            return PENDING;
        }

        for (String validStatus : VALID_STATUSES) {
            if (validStatus.equalsIgnoreCase(trimmed)) {
                return validStatus;
            }
        }

        return trimmed;
    }

    private LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public int getOrderID() {
        return orderID;
    }

    @Override
    public int getQuantity() {
        return products.size();
    }

    @Override
    public Date getOrderDate() {
        return toDate(orderDate);
    }

    public LocalDate getOrderLocalDate() {
        return orderDate;
    }

    @Override
    public String getOrderStatus() {
        return status;
    }

    public String getStatus() {
        return status;
    }

    public int getCustomerID() {
        if (customerID > 0) {
            return customerID;
        }
        return customer == null ? 0 : customer.getUserID();
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        this.customerID = customer == null ? 0 : customer.getUserID();
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    @Override
    public Date getEstimatedDelivery() {
        return estimatedDelivery == null ? null : toDate(estimatedDelivery);
    }

    public LocalDate getEstimatedDeliveryLocalDate() {
        return estimatedDelivery;
    }

    @Override
    public OrderTypeReadOnly getOrderType() {
        return orderType;
    }

    public OrderType getOrderTypeValue() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType == null ? OrderType.STANDARD : orderType;
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

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public Shipment getShipment() {
        return shipment;
    }

    @Override
    public String toString() {
        return "Order{"
                + "orderID=" + orderID
                + ", quantity=" + getQuantity()
                + ", status='" + status + '\''
                + ", customerID=" + getCustomerID()
                + ", orderDate=" + orderDate
                + ", totalAmount=" + totalAmount
                + ", estimatedDelivery=" + estimatedDelivery
                + ", trackingNumber='" + trackingNumber + '\''
                + ", orderType=" + orderType
                + ", paid=" + paid
                + ", refunded=" + refunded
                + '}';
    }
}
