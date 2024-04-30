package Main;

import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 * The GenerateInfoFiles class processes sales data files and generates various reports based on the data.
 */

public class GenerateInfoFiles {
	
	/**
     * Main method that initiates the generation of reports.
     *
     * @param args Command-line arguments (not used).
     */

    public static void main(String[] args) {
    	// Specifies the folder containing the sales files per salesman
        String folderPath = "reports";

        // Get a list of .txt files in the folder
        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));

        if (files != null) {
        	// Data structures to store products, sales, and salesmen
            Map<String, Map<String, Integer>> salesData = new HashMap<>();
            Map<String, Product> products = readProductsFromFile("productos.txt");
            Map<String, String> salesmenNames = readSalesmenFromFile("vendedores.txt");

            // Process each sales file per salesman
            for (File file : files) {
                processSalesData(file, salesData, products);
            }

            // Generate the unified report in CSV format
            generateUnifiedReport(salesData, products, salesmenNames);
            // Generate the total sales report
            generateTotalSalesReport(salesData, salesmenNames, products); 
            // Generate the product sales report
            generateProductSalesReport(salesData, products);
        } else {
            System.err.println("No se encontraron archivos en la carpeta especificada.");
        }
    }

    /**
     * Processes the sales data from a file and updates the salesData map.
     *
     * @param file       The file containing sales data.
     * @param salesData  The map to store sales data.
     * @param products   The map containing product information.
     */
    
    
    private static void processSalesData(File file, Map<String, Map<String, Integer>> salesData, Map<String, Product> products) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Leer la primera línea para obtener el tipo y número de documento del vendedor
            String firstLine = reader.readLine();
            if (firstLine != null) {
                String[] firstLineParts = firstLine.split(";");
                String tipoDocumento = firstLineParts[0];
                String numeroDocumento = firstLineParts[1];

                // Inicializar las ventas del vendedor en el mapa si es necesario
                String salesmanKey = tipoDocumento + "_" + numeroDocumento;
                salesData.putIfAbsent(salesmanKey, new HashMap<>());

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    // Leer los productos vendidos y sus cantidades
                    String productId = parts[0];
                    int quantitySold = Integer.parseInt(parts[1]);
                    salesData.get(salesmanKey).put(productId, salesData.get(salesmanKey).getOrDefault(productId, 0) + quantitySold);
                }
            } else {
            	System.err.println("El archivo '" + file.getName() + "' está vacío o no contiene la primera línea requerida.");
            }
        } catch (IOException e) {
            System.err.println("Error al procesar el archivo: " + file.getName() + ", tipo de error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Reads product information from a file and returns a map of products.
     *
     * @param fileName The name of the file containing product information.
     * @return A map containing product information.
     */

    private static Map<String, Product> readProductsFromFile(String fileName) {
        Map<String, Product> products = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                String id = parts[0];
                String name = parts[1];
                long price = Long.parseLong(parts[2]);
                products.put(id, new Product(id, name, price));
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de productos: " + fileName + ", tipo de error: " + e.getMessage());
            e.printStackTrace();
        }
        return products;
    }

    /**
     * Reads salesman information from a file and returns a map of salesmen.
     *
     * @param fileName The name of the file containing salesman information.
     * @return A map containing salesman information.
     */
    private static Map<String, String> readSalesmenFromFile(String fileName) {
        Map<String, String> salesmenNames = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                String tipoDocumento = parts[0];
                String numeroDocumento = parts[1];
                String nombre = parts[2];
                salesmenNames.put(tipoDocumento + "_" + numeroDocumento, nombre);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de vendedores: " + fileName + ", tipo de error: " + e.getMessage());
            e.printStackTrace();
        }
        return salesmenNames;
    }

    /**
     * Generates a unified report in CSV format based on sales data, product information, and salesman names.
     *
     * @param salesData     The sales data map.
     * @param products      The map containing product information.
     * @param salesmenNames The map containing salesman names.
     */
    private static void generateUnifiedReport(Map<String, Map<String, Integer>> salesData, Map<String, Product> products, Map<String, String> salesmenNames) {
    	String folderPath = "generatedReports";
        new File(folderPath).mkdirs();
    	
    	String fileName = folderPath + "/informe_unificado.csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("TipoDocumento,NúmeroDocumento,NombreVendedor,Producto,NombreProducto,PrecioUnitario,Cantidad,ValorTotal");

            // Escribir los datos de ventas en el archivo CSV
            for (Map.Entry<String, Map<String, Integer>> entry : salesData.entrySet()) {
                String[] parts = entry.getKey().split("_");
                String tipoDocumento = parts[0];
                String numeroDocumento = parts[1];
                String salesmanKey = tipoDocumento + "_" + numeroDocumento;
                String salesmanName = salesmenNames.getOrDefault(salesmanKey, "Desconocido");

                for (Map.Entry<String, Integer> saleEntry : entry.getValue().entrySet()) {
                    String productId = saleEntry.getKey();
                    int quantitySold = saleEntry.getValue();

                    // Obtener el nombre y el precio unitario del producto
                    Product product = products.get(productId);
                    if (product != null) {
                    
	                    String productName = product.getName();
	                    long unitPrice = product.getUnitPrice();
	
	                    // Calcular el valor total de las ventas del producto
	                    long totalValue = unitPrice * quantitySold;
	                    writer.printf("%s,%s,%s,%s,%s,%d,%d,%d%n", tipoDocumento, numeroDocumento, salesmanName, productId, productName, unitPrice, quantitySold, totalValue);
                    }else {
                    	System.err.println("El producto con ID '" + productId + "' no existe en la base de datos.");
                    }
                }
            }
            System.out.println("Informe unificado generado correctamente: " + fileName);
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo de informe unificado: " + fileName + ", tipo de error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Generates a total sales report in CSV format based on sales data and salesman names.
     *
     * @param salesData     The sales data map.
     * @param salesmenNames The map containing salesman names.
     * @param products      The map containing product information.
     */
    
    private static void generateTotalSalesReport(Map<String, Map<String, Integer>> salesData, Map<String, String> salesmenNames, Map<String, Product> products) {
        String folderPath = "generatedReports";
        new File(folderPath).mkdirs(); // Crear la carpeta si no existe
        
        String fileName = folderPath + "/informe_total_ventas.csv";
        
        // Crear una lista de vendedores con sus totales de ventas
        List<Salesman> salesmen = calculateTotalSalesForSalesmen(salesData, salesmenNames, products);
        
        // Ordenar la lista en función del total de ventas de cada vendedor (de mayor a menor)
        salesmen.sort((s1, s2) -> Long.compare(s2.getTotalSales(), s1.getTotalSales()));
        
        // Escribir los datos en el archivo CSV
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("TipoDocumento,NúmeroDocumento,NombreVendedor,ValorTotalVentas");
            for (Salesman salesman : salesmen) {
                writer.printf("%s,%s,%s,%d%n", salesman.getTypeID(), salesman.getDocumentNumber(), salesman.getName(), salesman.getTotalSales());
            }
            System.out.println("Informe de total de ventas generado correctamente: " + fileName);
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo de informe de total de ventas: " + fileName + ", tipo de error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Calculates total sales for each salesman and returns a list of Salesman objects.
     *
     * @param salesData     The sales data map.
     * @param salesmenNames The map containing salesman names.
     * @param products      The map containing product information.
     * @return A list of Salesman objects with total sales.
     */

    private static List<Salesman> calculateTotalSalesForSalesmen(Map<String, Map<String, Integer>> salesData, Map<String, String> salesmenNames, Map<String, Product> products) {
        List<Salesman> salesmen = new ArrayList<>();
        
        for (Map.Entry<String, Map<String, Integer>> entry : salesData.entrySet()) {
            String[] parts = entry.getKey().split("_");
            String tipoDocumento = parts[0];
            String numeroDocumento = parts[1];
            String salesmanKey = tipoDocumento + "_" + numeroDocumento;
            String salesmanName = salesmenNames.getOrDefault(salesmanKey, "Desconocido");

            // Calcular el valor total de las ventas del vendedor
            long totalSales = entry.getValue().entrySet().stream()
                    //.mapToLong(saleEntry -> saleEntry.getValue() * products.get(saleEntry.getKey()).getUnitPrice())            		
            		.mapToLong(saleEntry -> {
            		    Product product = products.get(saleEntry.getKey());
            		    if (product != null) {
            		        return saleEntry.getValue() * product.getUnitPrice();
            		    } else {
            		        System.err.println("El producto con ID '" + saleEntry.getKey() + "' no existe en la base de datos.");
            		        return 0; // O un valor predeterminado
            		    }
            		})
                    .sum();
            
            // Crear un objeto Salesman con los datos del vendedor y su total de ventas
            Salesman salesman = new Salesman(tipoDocumento, Long.parseLong(numeroDocumento), salesmanName, totalSales);
            salesmen.add(salesman);
        }
        
        return salesmen;
    }
    
    /**
     * Generates a product sales report in CSV format based on sales data and product information.
     *
     * @param salesData The sales data map.
     * @param products  The map containing product information.
     */
    private static void generateProductSalesReport(Map<String, Map<String, Integer>> salesData, Map<String, Product> products) {
        String folderPath = "generatedReports";
        new File(folderPath).mkdirs();
        
        String fileName = folderPath + "/informe_ventas_productos.csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("Producto,ValorUnitario,CantidadTotalVendida,ValorTotal");

            // Mapa para almacenar las ventas totales de cada producto
            Map<String, Long> totalSalesByProduct = new HashMap<>();

            // Calcular el total de ventas por producto sumando las ventas de cada vendedor
            for (Map<String, Integer> vendedorSales : salesData.values()) {
                for (Map.Entry<String, Integer> entry : vendedorSales.entrySet()) {
                    String productId = entry.getKey();
                    int quantitySold = entry.getValue();

                    // Verificar si el producto existe en la base de datos
                    Product product = products.get(productId);
                    if (product != null) {
                        long unitPrice = product.getUnitPrice();
                        long totalSales = unitPrice * quantitySold;
                        totalSalesByProduct.put(productId, totalSalesByProduct.getOrDefault(productId, 0L) + totalSales);
                    } else {
                        System.err.println("El producto con ID '" + productId + "' no existe en la base de datos.");
                    }
                }
            }

            // Ordenar los productos por total de ventas de mayor a menor
            List<Product> productList = new ArrayList<>();
            for (Map.Entry<String, Long> entry : totalSalesByProduct.entrySet()) {
                String productId = entry.getKey();
                long totalSales = entry.getValue();
                Product product = products.get(productId);

                product.setTotalSales(totalSales);
                productList.add(product);
            }
            productList.sort(Comparator.comparing(Product::getTotalSales).reversed());

            // Escribir los datos de ventas de productos en el archivo CSV
            for (Product product : productList) {
                long totalSales = totalSalesByProduct.get(product.getId());
                writer.printf("%s,%d,%d,%d%n", product.getName(), product.getUnitPrice(),
                        totalSales / product.getUnitPrice(),  totalSales);
            }
            System.out.println("Informe de ventas de productos generado correctamente: " + fileName);
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo de informe de ventas de productos: " + fileName + ", tipo de error: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
