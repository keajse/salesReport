package Main;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class Main {

	public static void main(String[] args) {
		GenerateInfoFiles.main(args);
		
		// Agregar mensaje al finalizar
        //System.out.println("Proceso completado exitosamente.");
        
     // Mostrar un MessageBox al finalizar
        JOptionPane.showMessageDialog(null, "Proceso completado exitosamente.");
    }
}

