package controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import java.time.format.DateTimeFormatter;
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
    @FXML private DatePicker txtFecha;
    @FXML private TextArea txtMensaje;

    private GestorCentralRecuerdos gestor = GestorCentralRecuerdos.getInstancia();

    @FXML
    public void initialize() {
        cmbCategoria.getItems().addAll(
                "Chisme", "Sueños", "Familia", "Salidas", "Estudio", "Trabajo", "Otros"
        );
        txtFecha.setEditable(false);
    }

    @FXML
    private void guardarRecuerdo(ActionEvent event) {
        try {
            String categoria = cmbCategoria.getValue();
            String descripcion = txtDescripcion.getText();
            String importanciaRaw = txtImportancia.getText();

            // --- FORMATO DE FECHA ---
            String fecha = "";
            if (txtFecha.getValue() != null) {
                fecha = txtFecha.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
            if (categoria == null || descripcion.isEmpty() || importanciaRaw.isEmpty() || fecha.isEmpty()) {
                txtMensaje.setText("Error: Complete todos los campos (incluyendo la fecha) antes de guardar.");
                return;
            }
            int importancia = Integer.parseInt(importanciaRaw);
            if (importancia < 1 || importancia > 10) {
                txtMensaje.setText("La importancia debe estar entre 1 y 10.");
                return;
            }
            Recuerdo nuevo = new Recuerdo(descripcion, importancia, fecha, categoria);
            String resultado = gestor.agregarRecuerdo(nuevo);
            txtMensaje.setText(resultado);
            if (resultado.contains("correctamente")) {
                limpiarFormulario();
            }
        } catch (NumberFormatException e) {
            txtMensaje.setText(" Error: La importancia debe ser un número entero.");
        } catch (Exception e) {
            txtMensaje.setText(" Ocurrió un error inesperado: " + e.getMessage());
        }
    }
    private void limpiarFormulario() {
        txtDescripcion.clear();
        txtImportancia.clear();
        txtFecha.setValue(null);
        cmbCategoria.getSelectionModel().clearSelection();
        txtDescripcion.requestFocus();
    }
    @FXML
    private void regresarMenu(ActionEvent event) {
        ClassBase.RetrocederMenu(Paths.MainMenu, "Menu Memory Core", event);
    }
}