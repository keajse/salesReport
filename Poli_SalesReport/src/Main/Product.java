package Main;

public class Product {
    private String id;
    private String name;
    private long unitPrice;
    private long totalQuantitySold;

    public Product(String id, String name, long unitPrice) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.totalQuantitySold = 0; // Inicialmente, la cantidad total vendida es cero
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(long unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    public long getTotalQuantitySold() {
        return totalQuantitySold;
    }

    // Método para registrar una venta del producto
    public void registerSale(int quantity) {
        totalQuantitySold += quantity;
    }
}

