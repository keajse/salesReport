package Main;

import java.util.Comparator;

/**
 * The Salesman class represents a salesperson with attributes such as name, document number,
 * total sales, type of identification, and last name.
 */
public class Salesman {
    
    // Attributes
    private String name; // Salesman's name
    private long documentNumber; // Salesman's document number
    private long totalSales; // Total sales of the salesman
    private String typeID; // Salesman's identification type
    private String lastName; // Salesman's last name

    /**
     * Constructor for the Salesman class.
     *
     * @param typeID        Salesman's identification type.
     * @param documentNumber Salesman's document number.
     * @param name          Salesman's name.
     * @param totalSales    Total sales of the salesman.
     */
    public Salesman(String typeID, long documentNumber, String name, long totalSales) {
        this.typeID = typeID;
        this.documentNumber = documentNumber;
        this.name = name;
        this.totalSales = totalSales;
    }
    
    // Accessor methods
    
    /**
     * Gets the type of identification of the salesman.
     *
     * @return The type of identification of the salesman.
     */
    public String getTypeID() {
        return typeID;
    }

    /**
     * Gets the name of the salesman.
     *
     * @return The name of the salesman.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the last name of the salesman.
     *
     * @return The last name of the salesman.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the document number of the salesman.
     *
     * @return The document number of the salesman.
     */
    public long getDocumentNumber() {
        return documentNumber;
    }

    /**
     * Gets the total sales of the salesman.
     *
     * @return The total sales of the salesman.
     */
    public long getTotalSales() {
        return totalSales;
    }

    // Additional methods
    
    /**
     * Adds sales to the total sales of the salesman.
     *
     * @param salesAmount The amount of sales to add.
     */
    public void addSales(long salesAmount) {
        this.totalSales += salesAmount;
    }

    /**
     * Static comparator to compare salesmen by total sales (from highest to lowest).
     */
    public static Comparator<Salesman> salesComparator = (s1, s2) -> Long.compare(s2.getTotalSales(), s1.getTotalSales());

}
