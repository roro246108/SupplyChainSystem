package supplychaintrackingsystem;

import java.util.Date;

public interface OrderReadOnly {
    int getOrderID();

    int getQuantity();

    Date getOrderDate();

    String getOrderStatus();

    int getCustomerID();

    Date getEstimatedDelivery();

    OrderTypeReadOnly getOrderType();
}
