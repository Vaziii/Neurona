package controllers;

import estructuras.MinHeap;
import estructuras.Nodo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import model.Recuerdo;
import sistema.GestorCentralRecuerdos;
import utilities.ClassBase;
import utilities.Paths;

public class OlvidoController {

    @FXML private TextArea txtAreaCandidatos;
    @FXML private Label lblMensajeOlvido;
    @FXML private ComboBox<String> cmbCategoriaOlvido;

    private GestorCentralRecuerdos gestor;
    private MinHeap colaPrioridad;

    @FXML
    public void initialize() {
        gestor = GestorCentralRecuerdos.getInstancia();

        // 1. Cargar las categorías en el ComboBox
        cmbCategoriaOlvido.getItems().addAll(
                "Chisme", "Sueños", "Familia", "Salidas", "Estudio", "Trabajo", "Otros"
        );

        // 2. AGREGAR LISTENER: Esto hace que al cambiar la selección, se actualice el texto automáticamente
        cmbCategoriaOlvido.setOnAction(event -> actualizarVista());

        // Carga inicial (muestra todo o vacío)
        actualizarVista();
    }

    private void actualizarVista() {
        txtAreaCandidatos.clear();
        colaPrioridad = new MinHeap(200); // Reiniciamos el Heap

        // Obtenemos la categoría que el usuario seleccionó (puede ser null al inicio)
        String categoriaFiltro = cmbCategoriaOlvido.getValue();

        StringBuilder sb = new StringBuilder();
        sb.append("===== VISTA DE MEMORIA: ").append(categoriaFiltro == null ? "COMPLETA" : categoriaFiltro.toUpperCase()).append(" =====\n");

        Nodo<Recuerdo> aux = gestor.getTodosLosRecuerdos().getCabeza();
        boolean hayRecuerdosEnCategoria = false;

        while (aux != null) {
            // SIEMPRE insertamos en el Heap para que el algoritmo de borrado tenga todos los datos disponibles
            colaPrioridad.insertar(aux.dato);

            // FILTRO VISUAL: Solo agregamos al TextArea si coincide con la categoría seleccionada
            // Si categoriaFiltro es null, mostramos todo.
            if (categoriaFiltro == null || aux.dato.getCategoria().equalsIgnoreCase(categoriaFiltro)) {
                sb.append(String.format("• [%s] %s (Imp: %d)\n",
                        aux.dato.getCategoria(),
                        aux.dato.getDescripcion(),
                        aux.dato.getImportancia()));
                hayRecuerdosEnCategoria = true;
            }

            aux = aux.siguiente;
        }

        if (!hayRecuerdosEnCategoria && categoriaFiltro != null) {
            sb.append("\n(No hay recuerdos en esta categoría)");
        }

        txtAreaCandidatos.setText(sb.toString());
    }

    @FXML
    private void ejecutarOlvido(ActionEvent event) {
        String categoriaSeleccionada = cmbCategoriaOlvido.getValue();

        if (categoriaSeleccionada == null) {
            lblMensajeOlvido.setText("⚠ Seleccione una categoría primero.");
            lblMensajeOlvido.setStyle("-fx-text-fill: red;");
            return;
        }

        if (colaPrioridad.estaVacio()) {
            lblMensajeOlvido.setText("La mente está vacía.");
            return;
        }

        // Ejecutamos la eliminación específica en el Heap
        Recuerdo olvidado = colaPrioridad.eliminarPorCategoria(categoriaSeleccionada);

        if (olvidado != null) {
            // Eliminamos del sistema principal (Lista y Archivo TXT)
            gestor.eliminarRecuerdo(olvidado);

            lblMensajeOlvido.setText("✅ SE HA OLVIDADO: " + olvidado.getDescripcion());
            lblMensajeOlvido.setStyle("-fx-text-fill: green;");

            // Refrescamos la vista (que ahora mostrará un recuerdo menos en esa categoría)
            actualizarVista();
        } else {
            lblMensajeOlvido.setText("ℹ No hay nada que borrar en: " + categoriaSeleccionada);
            lblMensajeOlvido.setStyle("-fx-text-fill: orange;");
        }
    }

    @FXML
    private void regresarMenu(ActionEvent event) {
        ClassBase.RetrocederMenu(Paths.MainMenu, "Menu Memory Core", event);
    }
}