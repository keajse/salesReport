package Main;

public class Salesman {
    /*private String firstName;
    private String lastName;
    private String documentType;
    private long documentNumber;
    private long totalSales;

    public Salesman(String firstName, String lastName, String documentType, long documentNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public long getDocumentNumber() {
        return documentNumber;
    }

    public long getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(long totalSales) {
        this.totalSales = totalSales;
    }*/
	
	private String name;
    private long documentNumber;
    private long totalSales;
    private String typeID;
    private String lastName;

    public Salesman(String typeID, long documentNumber, String name, String lastName) {
    	this.typeID = typeID;
    	this.documentNumber = documentNumber;
        this.name = name;
        this.lastName = lastName;
        
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

    public void setTotalSales(long totalSales) {
        this.totalSales = totalSales;
    }

}
