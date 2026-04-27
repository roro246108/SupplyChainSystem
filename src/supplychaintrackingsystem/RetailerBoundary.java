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
public class RetailerBoundary {

    private OrderController controller;

    public RetailerBoundary(OrderController controller) {
        this.controller = controller;
    }

    public Order requestCreateOrder(int retailerID, List<Product> products) {
        return controller.createOrder(retailerID, products);
    }

    public List<OrderReadOnly> requestViewOrders(int retailerID) {
        return controller.getOrders(retailerID);
    }

}
