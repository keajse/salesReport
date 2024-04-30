package Main;

import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 * The Main class contains the main method to start the application.
 */
public class Main {

    /**
     * The main method that starts the application by calling the main method of GenerateInfoFiles class.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        GenerateInfoFiles.main(args);
        
        // Display a message dialog upon completion
        JOptionPane.showMessageDialog(null, "Proceso completado exitosamente.");
    }
}
