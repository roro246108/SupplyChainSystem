package supplychaintrackingsystem;

import java.util.Date;

public class Invoice {
    private final int invoiceID;
    private final int orderID;
    private final double totalAmount;
    private final Date generatedAt;

    public Invoice(Order order) {
        this.invoiceID = order == null ? 0 : order.getOrderID();
        this.orderID = order == null ? 0 : order.getOrderID();
        this.totalAmount = order == null ? 0.0 : order.calculateTotal();
        this.generatedAt = new Date();
    }

    public int getInvoiceID() {
        return invoiceID;
    }

    public int getOrderID() {
        return orderID;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public Date getGeneratedAt() {
        return generatedAt;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceID=" + invoiceID +
                ", orderID=" + orderID +
                ", totalAmount=" + totalAmount +
                ", generatedAt=" + generatedAt +
                '}';
    }
}
