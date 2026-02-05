package controllers;

import estructuras.ListaSimple;
import estructuras.Nodo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import sistema.GestorCentralRecuerdos;
import utilities.ClassBase;
import utilities.Paths;

import java.io.IOException;

public class SeleccionCategoriaController {

    @FXML
    private ComboBox<String> comboCategorias;

    @FXML
    public void initialize() {
        // Cargar categorías disponibles
        ListaSimple<String> categorias = GestorCentralRecuerdos.getInstancia().getCategorias();
        Nodo<String> aux = categorias.getCabeza();
        while (aux != null) {
            comboCategorias.getItems().add(aux.dato);
            aux = aux.siguiente;
        }
    }

    @FXML
    private void visualizar() {
        String categoria = comboCategorias.getValue();
        if (categoria == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Paths.VisualizadorArbolView));
            Parent root = loader.load();

            // Pasamos la categoría al siguiente controlador
            VisualizadorArbol controller = loader.getController();
            controller.setCategoria(categoria);

            // Obtenemos el escenario (ventana) actual
            Stage stage = (Stage) comboCategorias.getScene().getWindow();

            // Cambiamos la escena
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Árbol de Recuerdos - " + categoria);

            // --- CAMBIO: VENTANA MAXIMIZADA ---
            stage.setMaximized(true);
            // ----------------------------------

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void regresar(ActionEvent event) {
        ClassBase.RetrocederMenu(Paths.MainMenu, "Menu Memory Core", event);
    }
}