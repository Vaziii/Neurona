package controllers;

import estructuras.MinHeap;
import estructuras.Nodo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import model.Recuerdo;
import sistema.GestorCentralRecuerdos;
import utilities.ClassBase;
import utilities.Paths;

public class OlvidoController {

    @FXML private TextArea txtAreaCandidatos;
    @FXML private Label lblMensajeOlvido;

    private GestorCentralRecuerdos gestor;
    private MinHeap colaPrioridad;

    @FXML
    public void initialize() {
        // Al crear el gestor, este ya llama internamente a cargarDesdeArchivos()
        gestor = new GestorCentralRecuerdos();
        actualizarVistaYPrioridades();
    }

    private void actualizarVistaYPrioridades() {
        txtAreaCandidatos.clear();
        colaPrioridad = new MinHeap(200);

        StringBuilder sb = new StringBuilder();
        sb.append("===== CONTENIDO CARGADO DESDE ARCHIVOS TXT =====\n");

        // Obtenemos la lista que el gestor ya llenó con cargarDesdeArchivos()
        Nodo<Recuerdo> aux = gestor.getTodosLosRecuerdos().getCabeza();

        if (aux == null) {
            sb.append("\n[!] No hay datos en los archivos o la carpeta está vacía.");
        }

        while (aux != null) {
            // Reutilizamos los datos cargados para llenar el TextArea visual
            sb.append(String.format("\n[%s] -> %s | Imp: %d",
                    aux.dato.getCategoria(),
                    aux.dato.getDescripcion(),
                    aux.dato.getImportancia()));

            // Llenamos el Heap para la política de olvido (Prioridad: importancia baja)
            colaPrioridad.insertar(aux.dato);

            aux = aux.siguiente;
        }

        txtAreaCandidatos.setText(sb.toString());
    }

    @FXML
    private void ejecutarOlvido(ActionEvent event) {
        if (colaPrioridad.estaVacio()) {
            lblMensajeOlvido.setText("Nada que eliminar.");
            return;
        }

        Recuerdo olvidado = colaPrioridad.extraerMinimo();

        if (olvidado != null) {
            // Usamos la función del gestor que borra de la lista y del TXT
            gestor.eliminarRecuerdo(olvidado);

            lblMensajeOlvido.setText("OLVIDADO: " + olvidado.getDescripcion());

            // Refrescamos todo
            actualizarVistaYPrioridades();
        }
    }

    @FXML
    private void regresarMenu(ActionEvent event) {
        ClassBase.RetrocederMenu(Paths.MainMenu, "Menu Memory Core", event);

    }
}