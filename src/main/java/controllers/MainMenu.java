package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import utilities.ClassBase;
import utilities.Paths;

import java.io.IOException;

public class MainMenu {

    @FXML private Button IdViajes;
    @FXML private Button IdIngreso;
    @FXML
    void EliminarR(ActionEvent event) {
        ClassBase.MenuButton(Paths.OlvidoView, "Olvidar Recuerdo", event);

    }
    @FXML
    void IngresoOpcion(ActionEvent event) {
        ClassBase.MenuButton(Paths.IngresoView, "Ingreso de Recuerdo", event);
    }
    @FXML
    public void VisualizarArb(ActionEvent event) {
        ClassBase.MenuButton(Paths.SeleccionCategoriaView,"Mostrar Arbol por Categorias", event);
    }
    @FXML
    void IrMenuView(ActionEvent event) {
        ClassBase.MenuButton(Paths.MainView, "Viajes Neuronales", event);
    }
    @FXML
    void salir(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
