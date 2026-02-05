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

/**
 * Clase base utilitaria para el manejo de navegacion entre vistas JavaFX.
 * Contiene metodos estaticos reutilizables para cambiar escenas.
 */
public abstract class ClassBase extends NeuronalesView {

    /**
     * Cambia de vista usando un boton u otro nodo como origen del evento.
     *
     * @param Path   ruta del archivo FXML a cargar
     * @param Titulo titulo que se mostrara en la ventana
     * @param event  evento que dispara la accion
     */
    public static void MenuButton(String Path, String Titulo, ActionEvent event) {
        try {
            // Carga la vista FXML indicada
            Parent root = FXMLLoader.load(ClassBase.class.getResource(Path));

            // Obtiene el Stage actual desde el evento
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Cambia la escena y configura la ventana
            stage.setScene(new Scene(root));
            stage.setTitle(Titulo);
            stage.setMaximized(false);
            stage.sizeToScene();
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            // Muestra el error en consola si falla la carga
            e.printStackTrace();
        }
    }

    /**
     * Permite regresar a un menu o vista anterior.
     * Soporta eventos provenientes de botones o MenuItem.
     *
     * @param fxml   ruta del archivo FXML
     * @param titulo titulo de la ventana
     * @param event  evento que origina la accion
     */
    public static void RetrocederMenu(String fxml, String titulo, ActionEvent event) {
        try {
            // Carga el FXML usando FXMLLoader
            FXMLLoader loader = new FXMLLoader(ClassBase.class.getResource(fxml));
            Parent root = loader.load();

            Stage stage;
            Object source = event.getSource();

            // Determina el origen del evento
            if (source instanceof Node node) {
                stage = (Stage) node.getScene().getWindow();
            } else if (source instanceof MenuItem menuItem) {
                stage = (Stage) menuItem.getParentPopup().getOwnerWindow();
            } else {
                throw new RuntimeException("No se pudo obtener el Stage.");
            }

            // Configura la ventana con la nueva escena
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.setMaximized(false);
            stage.sizeToScene();
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            // Muestra cualquier error ocurrido
            e.printStackTrace();
        }
    }
}
