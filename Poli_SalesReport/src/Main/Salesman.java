package Main;

import java.util.Comparator;

public class Salesman {
 
	
	private String name;
    private long documentNumber;
    private long totalSales;
    private String typeID;
    private String lastName;

    public Salesman(String typeID, long documentNumber, String name, long totalSales) {
    	this.typeID = typeID;
    	this.documentNumber = documentNumber;
        this.name = name;
        //this.lastName = lastName;
        this.totalSales = totalSales;
        
    }
    
    public String getTypeID() {
    	return typeID;
    }

    public String getName() {
        return name;
    }
    
    public String getLastName() {
        return lastName;
    }

    public long getDocumentNumber() {
        return documentNumber;
    }

    public long getTotalSales() {
        return totalSales;
    }

    
 // Método para agregar ventas al total
    public void addSales(long salesAmount) {
        this.totalSales += salesAmount;
    }

    // Método para comparar los vendedores por total de ventas (de mayor a menor)
    public static Comparator<Salesman> salesComparator = (s1, s2) -> Long.compare(s2.getTotalSales(), s1.getTotalSales());

}
