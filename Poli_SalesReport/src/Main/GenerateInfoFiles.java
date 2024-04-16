package Main;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class GenerateInfoFiles {

    public static void main(String[] args) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFileOrDirectory = fileChooser.getSelectedFile();
            if (selectedFileOrDirectory.isDirectory()) {
                processDirectory(selectedFileOrDirectory);
            } else {
                processFile(selectedFileOrDirectory);
            }
        } else {
            System.out.println("No se seleccionó ningún archivo o carpeta.");
        }
    }

    private static void processDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    processFile(file);
                }
            }
        }
    }

    private static void processFile(File file) {
        // Aquí se podría implementar la lógica para leer y procesar un archivo de vendedores
        System.out.println("Procesando archivo: " + file.getAbsolutePath());
    }

    public static void createSalesMenFile(int randomSalesCount, String name, long id) {
        String fileName = "vendedores_" + id + ".txt";
        try (FileWriter writer = new FileWriter(fileName)) {
            Random random = new Random();
            for (int i = 1; i <= randomSalesCount; i++) {
                writer.write("CC;" + id + ";" + name + " " + i + ";Apellido" + i + "\n");
                // Generar ventas aleatorias para este vendedor (opcional)
                createSalesForSeller(id, i);
            }
            System.out.println("Archivo de vendedores generado exitosamente: " + fileName);
        } catch (IOException e) {
            System.err.println("Error al generar el archivo de vendedores: " + e.getMessage());
        }
    }

    private static void createSalesForSeller(long sellerId, int salesCount) {
        String fileName = "ventas_vendedor_" + sellerId + ".txt";
        try (FileWriter writer = new FileWriter(fileName)) {
            Random random = new Random();
            for (int i = 1; i <= salesCount; i++) {
                String productId = "Producto" + (i * 2 - 1);
                int quantitySold = random.nextInt(10) + 1; // Cantidad vendida aleatoria
                writer.write(productId + ";" + quantitySold + "\n");
            }
            System.out.println("Archivo de ventas generado exitosamente: " + fileName);
        } catch (IOException e) {
            System.err.println("Error al generar el archivo de ventas para el vendedor " + sellerId + ": " + e.getMessage());
        }
    }

    public static void createProductsFile(int productsCount) {
        String fileName = "productos.txt";
        try (FileWriter writer = new FileWriter(fileName)) {
            Random random = new Random();
            for (int i = 1; i <= productsCount; i++) {
                String productId = "Producto" + i;
                String productName = "Producto " + i;
                double price = (random.nextDouble() * 100) + 1; // Precio aleatorio entre 1 y 100
                writer.write(productId + ";" + productName + ";" + price + "\n");
            }
            System.out.println("Archivo de productos generado exitosamente: " + fileName);
        } catch (IOException e) {
            System.err.println("Error al generar el archivo de productos: " + e.getMessage());
        }
    }
}
