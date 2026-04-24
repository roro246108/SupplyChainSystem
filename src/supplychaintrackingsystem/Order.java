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
    private Date estimatedDelivery;
    private String trackingNumber;//rawan
    private OrderType orderType;
   private Customer customer;// rawan
 private Shipment shipment; //rawan
    

    public Order() {
        this.orderDate = new Date();
        this.status = "NEW";
        this.orderType = OrderType.STANDARD;
    }
      
    //rawan 
    public Order(int orderID, Customer customer, List<Product> products) {
    this.orderID = orderID;
    this.customer = customer;
    this.orderDate = new Date();
    this.status = "NEW";
    this.orderType = OrderType.STANDARD;

    setProducts(products); 
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
        return "Order{" +
                "orderID=" + orderID +
                ", quantity=" + quantity +
                ", orderDate=" + orderDate +
                ", status='" + status + '\'' +
                ", customerID=" + customer +
                ", estimatedDelivery=" + estimatedDelivery +
                ", trackingNumber='" + trackingNumber + '\'' +
                ", orderType=" + orderType +
                '}';
    }
}
