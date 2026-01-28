package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Recuerdo;
import sistema.GestorCentralRecuerdos;
import utilities.ClassBase;
import utilities.Paths;

public class IngresoController {

    @FXML private ComboBox<String> cmbCategoria;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtImportancia;
    @FXML private TextField txtFecha;
    @FXML private TextArea txtMensaje;

    private GestorCentralRecuerdos gestor = new GestorCentralRecuerdos();

    @FXML
    public void initialize() {
        cmbCategoria.getItems().addAll(
                "chisme", "sueños", "familia", "salidas", "estudios", "viajes"
        );
    }

    @FXML
    private void guardarRecuerdo(ActionEvent event) {
        try {
            String categoria = cmbCategoria.getValue();
            String descripcion = txtDescripcion.getText();
            int importancia = Integer.parseInt(txtImportancia.getText());
            String fecha = txtFecha.getText();

            if (categoria == null || descripcion.isEmpty() || fecha.isEmpty()) {
                txtMensaje.setText("Complete todos los campos.");
                return;
            }

            Recuerdo nuevo = new Recuerdo(descripcion, importancia, fecha, categoria);
            boolean agregado = gestor.agregarRecuerdo(nuevo);

            if (!agregado) {

                txtMensaje.setText("Límite de 30 recuerdos alcanzado para esta categoría.");
                return;
            }

            txtMensaje.setText("Recuerdo guardado correctamente.");
            txtDescripcion.clear();
            txtImportancia.clear();
            txtFecha.clear();
            cmbCategoria.getSelectionModel().clearSelection();

        } catch (NumberFormatException e) {
            txtMensaje.setText("La importancia debe ser un número.");
        }
    }

    @FXML
    private void regresarMenu(ActionEvent event) {
        ClassBase.RetrocederMenu(Paths.MainMenu, "Menu Memory Core", event);
    }
}
