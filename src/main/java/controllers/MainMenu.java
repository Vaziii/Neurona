package controllers;

// Importaciones necesarias para manejar eventos, escenas y controles JavaFX
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

// Clases utilitarias propias del proyecto
import utilities.ClassBase;
import utilities.Paths;

public class MainMenu {

    // Boton para acceder a la vista de viajes neuronales
    @FXML
    private Button IdViajes;

    // Boton para acceder a la vista de ingreso de recuerdos
    @FXML
    private Button IdIngreso;

    // Metodo que se ejecuta al presionar la opción para eliminar (olvidar) un recuerdo
    // Redirige a la vista OlvidoView
    @FXML
    void EliminarR(ActionEvent event) {
        ClassBase.MenuButton(Paths.OlvidoView, "Olvidar Recuerdo", event);
    }

    // Metodo que se ejecuta al presionar la opción para ingresar un nuevo recuerdo
    // Abre la vista IngresoView
    @FXML
    void IngresoOpcion(ActionEvent event) {
        ClassBase.MenuButton(Paths.IngresoView, "Ingreso de Recuerdo", event);
    }

    // Metodo que permite visualizar los recuerdos organizados en un árbol por categorías
    // Redirige a la vista SeleccionCategoriaView
    @FXML
    public void VisualizarArb(ActionEvent event) {
        ClassBase.MenuButton(Paths.SeleccionCategoriaView, "Mostrar Arbol por Categorias", event);
    }

    // Metodo que dirige a la vista principal de viajes neuronales
    @FXML
    void IrMenuView(ActionEvent event) {
        ClassBase.MenuButton(Paths.MainView, "Viajes Neuronales", event);
    }

    // Metodo para cerrar completamente la aplicación
    // Obtiene la ventana actual y la cierra
    @FXML
    void salir(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
