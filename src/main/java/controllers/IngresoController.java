package controllers;

// Importaciones necesarias para manejar eventos y controles JavaFX
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

// Clases del modelo y lógica del sistema
import model.Recuerdo;
import sistema.GestorCentralRecuerdos;

// Clases utilitarias para navegación entre vistas
import utilities.ClassBase;
import utilities.Paths;

public class IngresoController {

    // ComboBox para seleccionar la categoría del recuerdo
    @FXML
    private ComboBox<String> cmbCategoria;

    // Campo de texto para ingresar la descripción del recuerdo
    @FXML
    private TextField txtDescripcion;

    // Campo de texto para ingresar la importancia del recuerdo
    @FXML
    private TextField txtImportancia;

    // Selector de fecha para el recuerdo
    @FXML
    private DatePicker txtFecha;

    // Área de texto para mostrar mensajes al usuario
    @FXML
    private TextArea txtMensaje;

    // Instancia única del gestor central de recuerdos (patrón Singleton)
    private GestorCentralRecuerdos gestor = GestorCentralRecuerdos.getInstancia();

    // Metodo que se ejecuta automáticamente al cargar la vista
    @FXML
    public void initialize() {
        // Se cargan las categorías disponibles en el ComboBox
        cmbCategoria.getItems().addAll(
                "Chisme", "Sueños", "Familia", "Salidas", "Estudio", "Trabajo", "Otros");

        // Se evita que la fecha sea escrita manualmente
        txtFecha.setEditable(false);
    }

    // Metodo que se ejecuta al presionar el botón Guardar
    @FXML
    private void guardarRecuerdo(ActionEvent event) {
        try {
            // Obtención de los valores ingresados por el usuario
            String categoria = cmbCategoria.getValue();
            String descripcion = txtDescripcion.getText();
            String importanciaRaw = txtImportancia.getText();

            // Conversión de la fecha seleccionada a String
            String fecha = "";
            if (txtFecha.getValue() != null) {
                fecha = txtFecha.getValue()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }

            // Validación de campos obligatorios
            if (categoria == null || descripcion.isEmpty()
                    || importanciaRaw.isEmpty() || fecha.isEmpty()) {
                txtMensaje.setText(
                        "Error: Complete todos los campos (incluyendo la fecha) antes de guardar.");
                return;
            }

            // Conversión y validación del nivel de importancia
            int importancia = Integer.parseInt(importanciaRaw);
            if (importancia < 1 || importancia > 10) {
                txtMensaje.setText("La importancia debe estar entre 1 y 10.");
                return;
            }

            // Creación del nuevo recuerdo
            Recuerdo nuevo = new Recuerdo(descripcion, importancia, fecha, categoria);

            // Se envia el recuerdo al gestor central
            String resultado = gestor.agregarRecuerdo(nuevo);

            // Se muestra el resultado al usuario
            txtMensaje.setText(resultado);

            // Si el recuerdo se guardó correctamente, se limpia el formulario
            if (resultado.contains("correctamente")) {
                limpiarFormulario();
            }

        } catch (NumberFormatException e) {
            // Error si la importancia no es un número entero
            txtMensaje.setText(" Error: La importancia debe ser un número entero.");
        } catch (Exception e) {
            // Captura de cualquier error inesperado
            txtMensaje.setText(" Ocurrió un error inesperado: " + e.getMessage());
        }
    }

    // Metodo para limpiar todos los campos del formulario
    private void limpiarFormulario() {
        txtDescripcion.clear();
        txtImportancia.clear();
        txtFecha.setValue(null);
        cmbCategoria.getSelectionModel().clearSelection();
        txtDescripcion.requestFocus();
    }

    // Metodo para regresar al menú principal
    @FXML
    private void regresarMenu(ActionEvent event) {
        ClassBase.RetrocederMenu(Paths.MainMenu, "Menu Memory Core", event);
    }
}
