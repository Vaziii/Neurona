package controllers;

// Importa estructuras propias
import estructuras.ListaSimple;
import estructuras.Nodo;

// Importaciones de JavaFX
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

// Importa el gestor central del sistema
import sistema.GestorCentralRecuerdos;

// Importa utilidades de navegacion
import utilities.ClassBase;
import utilities.Paths;

// Importaciones de manejo de errores
import java.io.IOException;

// Controlador para seleccionar una categoria de recuerdos
public class SeleccionCategoriaController {

    // ComboBox que muestra las categorias disponibles
    @FXML
    private ComboBox<String> comboCategorias;

    // Metodo que se ejecuta al iniciar la vista
    @FXML
    public void initialize() {

        // Obtiene la lista de categorias desde el gestor central
        ListaSimple<String> categorias = GestorCentralRecuerdos.getInstancia().getCategorias();

        // Recorre la lista simple y carga las categorias en el ComboBox
        Nodo<String> aux = categorias.getCabeza();
        while (aux != null) {
            comboCategorias.getItems().add(aux.dato);
            aux = aux.siguiente;
        }
    }

    // Abre el visualizador del arbol para la categoria seleccionada
    @FXML
    private void visualizar() {

        // Obtiene la categoria seleccionada
        String categoria = comboCategorias.getValue();

        // Si no hay categoria seleccionada no hace nada
        if (categoria == null)
            return;

        try {
            // Carga la vista del visualizador del arbol
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(Paths.VisualizadorArbolView)
            );
            Parent root = loader.load();

            // Obtiene el controlador del visualizador
            VisualizadorArbol controller = loader.getController();

            // Asigna la categoria seleccionada
            controller.setCategoria(categoria);

            // Obtiene la ventana actual
            Stage stage = (Stage) comboCategorias.getScene().getWindow();

            // Crea y asigna la nueva escena
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Cambia el titulo de la ventana
            stage.setTitle("Arbol de Recuerdos - " + categoria);

            // Maximiza la ventana
            stage.setMaximized(true);

        } catch (IOException e) {
            // Muestra el error en consola
            e.printStackTrace();
        }
    }

    // Regresa al menu principal
    @FXML
    private void regresar(ActionEvent event) {

        // Navega a la vista del menu principal
        ClassBase.RetrocederMenu(
                Paths.MainMenu,
                "Menu Memory Core",
                event
        );
    }
}
