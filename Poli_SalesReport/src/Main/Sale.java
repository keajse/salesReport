package Main;

public class Sale {
    private String productId;
    private int quantitySold;

    public Sale(String productId, int quantitySold) {
        this.productId = productId;
        this.quantitySold = quantitySold;
    }

    // Getters and Setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }
}

