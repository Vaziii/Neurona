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
        /**try {
            Parent root = FXMLLoader.load(getClass().getResource(Paths.IngresoView));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
            stage.setTitle("Ingreso de Recuerdos");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @FXML
    void IrMenuView(ActionEvent event) {
        ClassBase.MenuButton(Paths.MainView, "Viajes Neuronales", event);
        /**try {
            Parent root = FXMLLoader.load(getClass().getResource(Paths.MainView));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 650));
            stage.setTitle("Viajes Neuronales");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @FXML
    void salir(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void VisualizarArb(ActionEvent event) {
        ClassBase.MenuButton(Paths.VisualizadorView, "Visualizador de Arbol", event);
    }
}
