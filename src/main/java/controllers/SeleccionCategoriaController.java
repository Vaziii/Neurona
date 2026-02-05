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

            VisualizadorArbol controller = loader.getController();
            controller.setCategoria(categoria);

            Stage stage = (Stage) comboCategorias.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Árbol de Recuerdos - " + categoria);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void regresar() {
        Stage stage = (Stage) comboCategorias.getScene().getWindow();
        stage.close();
    }

}
