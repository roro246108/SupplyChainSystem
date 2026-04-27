package supplychaintrackingsystem;

public class OrderBoundary {
    private final OrderController controller;

    public OrderBoundary(OrderController controller) {
        if (controller == null) {
            throw new NullPointerException("Order controller cannot be null.");
        }

        this.controller = controller;
    }

    public Product saveProduct(Product product) {
        return controller.saveProduct(product);
    }

    public Product loadProduct(int productID) {
        return controller.loadProduct(productID);
    }

    public boolean checkAvailability(Product product) {
        return controller.checkProductAvailability(product);
    }

    public Product reportIssue(Product product, String issue) {
        return controller.reportProductIssue(product, issue);
    }

    public Product getCurrentProduct() {
        return controller.getCurrentProduct();
    }

    public void clearCurrentProduct() {
        controller.clearCurrentProduct();
    }
}
