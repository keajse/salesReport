package Main;

import java.util.Comparator;

/**
 * The Product class represents a product with attributes such as ID, name, unit price,
 * total quantity sold, and total sales.
 */
public class Product {
    private String id; // Product ID
    private String name; // Product name
    private long unitPrice; // Unit price of the product
    private long totalQuantitySold; // Total quantity of the product sold
    private long totalSales; // Total sales of the product

    /**
     * Constructor for the Product class.
     *
     * @param id        The product ID.
     * @param name      The product name.
     * @param unitPrice The unit price of the product.
     */
    public Product(String id, String name, long unitPrice) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.totalQuantitySold = 0; // Initially, the total quantity sold is zero
        this.totalSales = 0;
    }

    // Getters and Setters

    /**
     * Gets the product ID.
     *
     * @return The product ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the product ID.
     *
     * @param id The product ID to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the product name.
     *
     * @return The product name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the product name.
     *
     * @param name The product name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the unit price of the product.
     *
     * @return The unit price of the product.
     */
    public long getUnitPrice() {
        return unitPrice;
    }

    /**
     * Sets the unit price of the product.
     *
     * @param unitPrice The unit price of the product to set.
     */
    public void setUnitPrice(long unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * Gets the total quantity of the product sold.
     *
     * @return The total quantity of the product sold.
     */
    public long getTotalQuantitySold() {
        return totalQuantitySold;
    }

    /**
     * Gets the total sales of the product.
     *
     * @return The total sales of the product.
     */
    public long getTotalSales() {
        return totalSales;
    }

    /**
     * Sets the total sales of the product.
     *
     * @param totalSales The total sales of the product to set.
     */
    public void setTotalSales(long totalSales) {
        this.totalSales = totalSales;
    }

    /**
     * Registers a sale of the product.
     *
     * @param quantity The quantity of the product sold.
     */
    public void registerSale(int quantity) {
        totalQuantitySold += quantity;
        totalSales += quantity * unitPrice; // Update the total sales
    }
}

