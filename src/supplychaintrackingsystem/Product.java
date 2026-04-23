package supplychaintrackingsystem;

public class Product {
    private String productName;
    private double basePrice;
    private String description;
    private String category;
    private String status;

    public Product() {
    }

    public Product(String productName, double basePrice, String description, String category) {
        this.productName = productName;
        this.basePrice = basePrice;
        this.description = description;
        this.category = category;
    }

    public void createProduct(String productData) {
        this.description = productData;
    }

    public void updateProductStatus(String newStatus) {
        this.status = newStatus;
    }

    public void viewProductDetails() {
        System.out.println(toString());
    }

    public void assignProduct(String productID, int retailerID) {
        System.out.println("Assign product " + productID + " to retailer " + retailerID);
    }

    public String getProductName() {
        return productName;
    }

    public double getBasePrice() {
        return basePrice;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productName='" + productName + '\'' +
                ", basePrice=" + basePrice +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
