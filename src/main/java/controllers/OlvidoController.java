package controllers;

// Importa estructuras de datos
import estructuras.MinHeap;
import estructuras.Nodo;

// Importaciones de JavaFX
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

// Importa el modelo Recuerdo
import model.Recuerdo;

// Importa el gestor central del sistema
import sistema.GestorCentralRecuerdos;

// Importa utilidades de navegacion
import utilities.ClassBase;
import utilities.Paths;

// Controlador encargado del proceso de olvido inteligente
public class OlvidoController {

    // Area de texto donde se muestran los recuerdos candidatos al olvido
    @FXML
    private TextArea txtAreaCandidatos;

    // Etiqueta para mostrar mensajes al usuario
    @FXML
    private Label lblMensajeOlvido;

    // ComboBox para seleccionar la categoria a olvidar
    @FXML
    private ComboBox<String> cmbCategoriaOlvido;

    // Referencia al gestor central de recuerdos
    private GestorCentralRecuerdos gestor;

    // Cola de prioridad que ordena los recuerdos por importancia
    private MinHeap colaPrioridad;

    // Metodo que se ejecuta al iniciar la vista
    @FXML
    public void initialize() {

        // Obtiene la instancia unica del gestor central
        gestor = GestorCentralRecuerdos.getInstancia();

        // Carga las categorias disponibles en el ComboBox
        cmbCategoriaOlvido.getItems().addAll(
                "Chisme", "Sueños", "Familia", "Salidas", "Estudio", "Trabajo", "Otros"
        );

        // Actualiza la vista cuando se cambia la categoria seleccionada
        cmbCategoriaOlvido.setOnAction(event -> actualizarVista());

        // Inicializa la vista
        actualizarVista();
    }

    // Actualiza la lista de recuerdos visibles y la cola de prioridad
    private void actualizarVista() {

        // Limpia el area de texto
        txtAreaCandidatos.clear();

        // Inicializa la cola de prioridad con capacidad amplia
        colaPrioridad = new MinHeap(200);

        // Obtiene la categoria seleccionada como filtro
        String categoriaFiltro = cmbCategoriaOlvido.getValue();

        // Construye el texto a mostrar
        StringBuilder sb = new StringBuilder();
        sb.append("===== VISTA DE MEMORIA: ")
                .append(categoriaFiltro == null ? "COMPLETA" : categoriaFiltro.toUpperCase())
                .append(" =====\n");

        // Recorre todos los recuerdos almacenados
        Nodo<Recuerdo> aux = gestor.getTodosLosRecuerdos().getCabeza();
        boolean hayRecuerdosEnCategoria = false;

        while (aux != null) {

            // Inserta cada recuerdo en la cola de prioridad
            colaPrioridad.insertar(aux.dato);

            // Aplica el filtro por categoria si existe
            if (categoriaFiltro == null ||
                    aux.dato.getCategoria().equalsIgnoreCase(categoriaFiltro)) {

                sb.append(String.format(
                        "• [%s] %s (Imp: %d)\n",
                        aux.dato.getCategoria(),
                        aux.dato.getDescripcion(),
                        aux.dato.getImportancia()
                ));
                hayRecuerdosEnCategoria = true;
            }

            aux = aux.siguiente;
        }

        // Mensaje si no existen recuerdos en la categoria seleccionada
        if (!hayRecuerdosEnCategoria && categoriaFiltro != null) {
            sb.append("\n(No hay recuerdos en esta categoria)");
        }

        // Muestra el contenido final en el area de texto
        txtAreaCandidatos.setText(sb.toString());
    }

    // Ejecuta el proceso de olvido segun la categoria seleccionada
    @FXML
    private void ejecutarOlvido(ActionEvent event) {

        // Obtiene la categoria seleccionada
        String categoriaSeleccionada = cmbCategoriaOlvido.getValue();

        // Valida que exista una categoria seleccionada
        if (categoriaSeleccionada == null) {
            lblMensajeOlvido.setText("Seleccione una categoria primero.");
            lblMensajeOlvido.setStyle("-fx-text-fill: red;");
            return;
        }

        // Verifica si la cola de prioridad esta vacia
        if (colaPrioridad.estaVacio()) {
            lblMensajeOlvido.setText("La mente esta vacia.");
            return;
        }

        // Elimina el recuerdo menos importante de la categoria seleccionada
        Recuerdo olvidado = colaPrioridad.eliminarPorCategoria(categoriaSeleccionada);

        if (olvidado != null) {

            // Elimina el recuerdo del gestor central
            gestor.eliminarRecuerdo(olvidado);

            // Muestra mensaje de confirmacion
            lblMensajeOlvido.setText("SE HA OLVIDADO: " + olvidado.getDescripcion());
            lblMensajeOlvido.setStyle("-fx-text-fill: green;");

            // Actualiza la vista
            actualizarVista();

        } else {
            // Mensaje cuando no hay recuerdos que borrar
            lblMensajeOlvido.setText(" No hay nada que borrar en: " + categoriaSeleccionada);
            lblMensajeOlvido.setStyle("-fx-text-fill: orange;");
        }
    }

    // Regresa al menu principal
    @FXML
    private void regresarMenu(ActionEvent event) {

        // Navega de regreso al menu principal
        ClassBase.RetrocederMenu(
                Paths.MainMenu,
                "Menu Memory Core",
                event
        );
    }
}
