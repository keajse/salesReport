package Main;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            List<Salesman> salesmen = readSalesmenFromFile("vendedores.txt");
            List<Product> products = readProductsFromFile("productos.txt");
            generateReport(selectedFile, salesmen, products);
            System.out.println("Informe generado con éxito.");
        } else {
            System.out.println("No se seleccionó ningún archivo.");
        }
    }

    private static List<Salesman> readSalesmenFromFile(String fileName) {
        List<Salesman> salesmen = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                String typeID = parts[0];
                String name = parts[2];
                String lastName = parts[3];
                long documentNumber = Long.parseLong(parts[1]);
                salesmen.add(new Salesman(typeID, documentNumber, name, lastName));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return salesmen;
    }

    private static List<Product> readProductsFromFile(String fileName) {
        List<Product> products = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                String id = parts[0];
                String name = parts[1];
                long price = Long.parseLong(parts[2]);
                products.add(new Product(id, name, price));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }

    private static void generateReport(File file, List<Salesman> salesmen, List<Product> products) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] salesmanParts = line.split(";");
                long documentNumber = Long.parseLong(salesmanParts[1]);
                Salesman salesman = findSalesmanByDocumentNumber(salesmen, documentNumber);
                if (salesman != null) {
                    Map<String, String> sales = new HashMap<>();
                    for (int i = 2; i < salesmanParts.length; i++) {
                        String[] productParts = salesmanParts[i].split(",");
                        String productId = productParts[0];
                        String quantitySold = productParts[1];
                        sales.put(productId, quantitySold);
                    }
                    generateSalesmanReport(salesman, sales, products);
                } else {
                    System.err.println("Vendedor con número de documento " + documentNumber + " no encontrado.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Salesman findSalesmanByDocumentNumber(List<Salesman> salesmen, long documentNumber) {
        for (Salesman salesman : salesmen) {
            if (salesman.getDocumentNumber() == documentNumber) {
                return salesman;
            }
        }
        return null;
    }

    private static void generateSalesmanReport(Salesman salesman, Map<String, String> sales, List<Product> products) {
        String fileName = "informe_" + salesman.getDocumentNumber() + ".csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("Producto,Cantidad,Valor Total");
            long totalSales = 0;
            for (Map.Entry<String, String> entry : sales.entrySet()) {
                String productId = entry.getKey();
                String quantitySold = entry.getValue();
                Product product = findProductById(products, productId);
                if (product != null) {
                    long totalValue = product.getUnitPrice() * Long.parseLong(quantitySold);
                    writer.printf("%s,%d,%d%n", product.getName(), quantitySold, totalValue);
                    totalSales += totalValue;
                }
            }
            writer.println("Total de ventas:," + totalSales);
            System.out.println("Informe generado correctamente: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Product findProductById(List<Product> products, String productId) {
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                return product;
            }
        }
        return null;
    }
}

