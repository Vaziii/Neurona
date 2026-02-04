package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker; // Importación necesaria
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Recuerdo;
import sistema.GestorCentralRecuerdos;
import utilities.ClassBase;
import utilities.Paths;

import java.time.format.DateTimeFormatter; // Para dar formato a la fecha

public class IngresoController {

    @FXML private ComboBox<String> cmbCategoria;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtImportancia;

    // Cambiamos TextField por DatePicker
    @FXML private DatePicker txtFecha;

    @FXML private TextArea txtMensaje;

    private GestorCentralRecuerdos gestor = new GestorCentralRecuerdos();

    @FXML
    public void initialize() {
        cmbCategoria.getItems().addAll(
                "Chisme", "Sueños", "Familia", "Salidas", "Estudio", "Trabajo", "Otros"
        );

        // Opcional: Evitar que el usuario escriba texto manualmente en el calendario
        txtFecha.setEditable(false);
    }

    @FXML
    private void guardarRecuerdo(ActionEvent event) {
        try {
            String categoria = cmbCategoria.getValue();
            String descripcion = txtDescripcion.getText();
            String importanciaRaw = txtImportancia.getText();

            // 1. Obtener y formatear la fecha del DatePicker
            String fecha = "";
            if (txtFecha.getValue() != null) {
                // Usamos el formato dd/MM/yyyy (puedes ajustarlo si prefieres otro)
                fecha = txtFecha.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            }

            // 2. Validaciones básicas
            if (categoria == null || descripcion.isEmpty() || importanciaRaw.isEmpty() || fecha.isEmpty()) {
                txtMensaje.setText("Error: Complete todos los campos (incluyendo la fecha) antes de guardar.");
                return;
            }

            int importancia = Integer.parseInt(importanciaRaw);

            if (importancia < 1 || importancia > 10) {
                txtMensaje.setText("La importancia debe estar entre 1 y 10.");
                return;
            }

            // 3. Crear el objeto con la fecha formateada
            Recuerdo nuevo = new Recuerdo(descripcion, importancia, fecha, categoria);

            // 4. Ejecutar lógica en el gestor
            String resultado = gestor.agregarRecuerdo(nuevo);

            // 5. Mostrar resultado
            txtMensaje.setText(resultado);

            // 6. Limpiar campos
            limpiarFormulario();

        } catch (NumberFormatException e) {
            txtMensaje.setText(" Error: La importancia debe ser un número entero.");
        } catch (Exception e) {
            txtMensaje.setText(" Ocurrió un error inesperado: " + e.getMessage());
        }
    }

    private void limpiarFormulario() {
        txtDescripcion.clear();
        txtImportancia.clear();
        // Limpiamos el DatePicker
        txtFecha.setValue(null);
        cmbCategoria.getSelectionModel().clearSelection();
        txtDescripcion.requestFocus();
    }

    @FXML
    private void regresarMenu(ActionEvent event) {
        ClassBase.RetrocederMenu(Paths.MainMenu, "Menu Memory Core", event);
    }
}