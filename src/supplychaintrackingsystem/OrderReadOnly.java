package supplychaintrackingsystem;

import java.util.Date;

public interface OrderReadOnly {
    int getOrderID();

    int getQuantity();

    Date getOrderDate();

    String getOrderStatus();

    Date getEstimatedDelivery();

    OrderTypeReadOnly getOrderType();
}
