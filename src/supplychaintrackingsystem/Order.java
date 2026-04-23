package supplychaintrackingsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Order implements OrderReadOnly {
    private int orderID;
    private final List<Product> products = new ArrayList<>();
    private int quantity;
    private Date orderDate;
    private String status;
    private int customerID;
    private Date estimatedDelivery;
    private String trackingNumber;
    private OrderType orderType;

    public Order() {
        this.orderDate = new Date();
        this.status = "NEW";
        this.orderType = OrderType.STANDARD;
    }

    public Order(int orderID, int customerID, List<Product> products) {
        this();
        this.orderID = orderID;
        this.customerID = customerID;
        setProducts(products);
    }

    public int getOrderID() {
        return orderID;
    }

    public int getQuantity() {
        return quantity;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public String getOrderStatus() {
        return status;
    }

    public int getCustomerID() {
        return customerID;
    }

    public Date getEstimatedDelivery() {
        return estimatedDelivery;
    }

    public OrderTypeReadOnly getOrderType() {
        return orderType;
    }

    public void updateOrderStatus(String status) {
        this.status = status;
    }

    public void setEstimatedDelivery(Date estimatedDelivery) {
        this.estimatedDelivery = estimatedDelivery;
    }

    public Invoice generateInvoice() {
        return new Invoice(this);
    }

    public double calculateTotal() {
        return products.stream().mapToDouble(Product::getBasePrice).sum();
    }

    public boolean cancelOrder() {
        this.status = "CANCELLED";
        return true;
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

    public void setProducts(List<Product> products) {
        this.products.clear();
        if (products != null) {
            this.products.addAll(products);
        }
        this.quantity = this.products.size();
    }

    public void addProduct(Product product) {
        if (product != null) {
            this.products.add(product);
            this.quantity = this.products.size();
        }
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderID=" + orderID +
                ", quantity=" + quantity +
                ", orderDate=" + orderDate +
                ", status='" + status + '\'' +
                ", customerID=" + customerID +
                ", estimatedDelivery=" + estimatedDelivery +
                ", trackingNumber='" + trackingNumber + '\'' +
                ", orderType=" + orderType +
                '}';
    }
}
