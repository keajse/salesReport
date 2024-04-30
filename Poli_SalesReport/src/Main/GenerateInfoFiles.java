package Main;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class GenerateInfoFiles {

    public static void main(String[] args) {
    	// Especifica la carpeta que contiene los archivos de ventas por vendedor
        String folderPath = "reports";

        // Obtener lista de archivos .txt en la carpeta
        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));

        if (files != null) {
            // Estructuras de datos para almacenar los productos, las ventas y los vendedores
            Map<String, Map<String, Integer>> salesData = new HashMap<>();
            Map<String, Product> products = readProductsFromFile("productos.txt");
            Map<String, String> salesmenNames = readSalesmenFromFile("vendedores.txt");

            // Procesar cada archivo de ventas por vendedor
            for (File file : files) {
                processSalesData(file, salesData, products);
            }

            // Generar el informe unificado en formato CSV
            generateUnifiedReport(salesData, products, salesmenNames);
            generateTotalSalesReport(salesData, salesmenNames, products); 
            generateProductSalesReport(salesData, products);
        } else {
            System.err.println("No se encontraron archivos en la carpeta especificada.");
        }
    }

    
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
