/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package supplychaintrackingsystem;

import java.util.List;

/**
 *
 * @author Rawan
 */
public class CustomerBoundary {
    
     private OrderController controller;

    public CustomerBoundary(OrderController controller) {
        this.controller = controller;
    }

    public Order requestCreateOrder(int customerID, List<Product> products) {
        return controller.createOrder(customerID, products);
    }

    public List<OrderReadOnly> requestViewOrders(int customerID) {
        return controller.getOrders(customerID);
    }

    public Order requestTrackOrder(int orderID) {
        return controller.getOrderByID(orderID);
    }
  
    
    
    
}
