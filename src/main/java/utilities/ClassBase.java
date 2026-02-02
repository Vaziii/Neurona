package utilities;

import controllers.NeuronalesView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class ClassBase extends NeuronalesView {
    public static void MenuButton(String Path, String Titulo, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(ClassBase.class.getResource(Path));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(Titulo);
            stage.show();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public static void RetrocederMenu(String fxml, String titulo, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(ClassBase.class.getResource(fxml));
            Parent root = loader.load();

            Stage stage;

            Object source = event.getSource();

            if (source instanceof Node node) {
                stage = (Stage) node.getScene().getWindow();
            } else if (source instanceof MenuItem menuItem) {
                stage = (Stage) menuItem.getParentPopup().getOwnerWindow();
            } else {
                throw new RuntimeException("No se pudo obtener el Stage.");
            }

            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void start(Stage stage) throws Exception;
}
