/**
 *Proyecto de sistema artificial de gestion de recuerdos
 *
 * @author Elkin Ibarra, Darwin Reyes, Joshua Murillo, Alfredo Romero
 * @version 1.0
 * @since 2026-02-02
 */


package application;

// Importación de clases principales de JavaFX
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Clase utilitaria que contiene las rutas a los archivos FXML
import utilities.Paths;

import java.net.URL;

public class App extends Application {

    // Metodo principal que se ejecuta al iniciar la aplicación JavaFX
    @Override
    public void start(Stage stage) throws Exception {

        // Se obtiene la ubicación del archivo FXML del menú principal
        URL fxmlLocation = getClass().getResource(Paths.MainMenu);

        // Validación: si el archivo FXML no existe, se lanza una excepción
        if (fxmlLocation == null) {
            throw new RuntimeException(
                    "No se pudo encontrar el archivo FXML en: " + Paths.MainMenu);
        }

        // Se carga el archivo FXML y se genera la jerarquía de nodos
        Parent root = FXMLLoader.load(fxmlLocation);

        // Se crea la escena principal usando el nodo raíz
        Scene scene = new Scene(root);

        // Se configura la ventana principal
        stage.setResizable(false);               // Evita que la ventana sea redimensionable
        stage.setTitle("Menu Memory Core");      // Título de la ventana
        stage.setScene(scene);                   // Asigna la escena al Stage
        stage.show();                            // Muestra la ventana
    }

    // Metodo main: punto de entrada de la aplicación
    public static void main(String[] args) {
        launch(args); // Lanza la aplicación JavaFX
    }
}
