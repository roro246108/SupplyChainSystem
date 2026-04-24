package supplychaintrackingsystem;

public class Product {
    private static int nextProductID = 1;

    private final int productID;
    private String productName;
    private double basePrice;
    private String description;
    private String category;
    private String status;

    public Product() {
        this.productID = nextProductID++;
    }

    public Product(String productName, double basePrice, String description, String category) {
        this();
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

    public int getProductID() {
        return productID;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public double getPrice() {
        return basePrice;
    }

    public String getStatus() {
        return status;
    }

    public boolean isAvailable() {
        return status == null || !"OUT_OF_STOCK".equalsIgnoreCase(status);
    }

    @Override
    public String toString() {
        return "Product{" +
                "productID=" + productID +
                ", " +
                "productName='" + productName + '\'' +
                ", basePrice=" + basePrice +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
