package Main;

/**
 * The Sale class represents a sale with attributes such as product ID and quantity sold.
 */
public class Sale {
    private String productId; // Product ID of the sale
    private int quantitySold; // Quantity of the product sold in the sale

    /**
     * Constructor for the Sale class.
     *
     * @param productId    The product ID of the sale.
     * @param quantitySold The quantity of the product sold in the sale.
     */
    public Sale(String productId, int quantitySold) {
        this.productId = productId;
        this.quantitySold = quantitySold;
    }

    // Getters and Setters

    /**
     * Gets the product ID of the sale.
     *
     * @return The product ID of the sale.
     */
    public String getProductId() {
        return productId;
    }

    /**
     * Sets the product ID of the sale.
     *
     * @param productId The product ID to set.
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * Gets the quantity of the product sold in the sale.
     *
     * @return The quantity of the product sold in the sale.
     */
    public int getQuantitySold() {
        return quantitySold;
    }

    /**
     * Sets the quantity of the product sold in the sale.
     *
     * @param quantitySold The quantity to set.
     */
    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }
}
