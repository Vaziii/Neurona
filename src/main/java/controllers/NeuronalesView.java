package controllers;

// Importa estructuras de datos
import estructuras.ListaDobleRecuerdos;
import estructuras.Nodo;

// Importaciones de JavaFX
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

// Importa el modelo Recuerdo
import model.Recuerdo;

// Importa el gestor central y modulos
import sistema.GestorCentralRecuerdos;
import sistema.Modulo;
import sistema.ModuloMemoriaReciente;

// Importa utilidades de navegacion
import utilities.ClassBase;
import utilities.Paths;

// Vista principal del sistema neuronal
// Implementa la interfaz Modulo para recibir actualizaciones
public class NeuronalesView implements Modulo {

    // Boton para regresar al menu
    @FXML
    private Button IdMenu;

    // Campos de entrada para crear recuerdos
    @FXML
    private TextField txtDescripcion;
    @FXML
    private TextField txtImportancia;
    @FXML
    private TextField txtFecha;

    // Campos de busqueda
    @FXML
    private TextField txtBuscarImportancia;
    @FXML
    private ComboBox<String> cmbBuscarCategoria;

    // Lista visual de recuerdos
    @FXML
    private ListView<String> listaRecuerdos;

    // Area para mostrar el recuerdo actual
    @FXML
    private TextArea txtRecuerdo;

    // Area para mostrar la memoria reciente
    @FXML
    private TextArea areaMemoriaReciente;

    // Referencia al gestor central
    private GestorCentralRecuerdos gestor;

    // Modulo encargado de la memoria reciente
    private ModuloMemoriaReciente moduloReciente;

    // Metodo que se ejecuta al iniciar la vista
    @FXML
    public void initialize() {

        // Obtiene la instancia del gestor central
        gestor = GestorCentralRecuerdos.getInstancia();

        // Crea e inicializa el modulo de memoria reciente
        moduloReciente = new ModuloMemoriaReciente();

        // Registra los modulos para recibir actualizaciones
        gestor.registrarModulo(moduloReciente);
        gestor.registrarModulo(this);

        // Carga las categorias disponibles para busqueda
        cmbBuscarCategoria.getItems().addAll(
                "Chisme", "Sueños", "Familia", "Salidas",
                "Estudio", "Trabajo", "Otros"
        );

        // Muestra todos los recuerdos al iniciar
        mostrarTodos();
    }

    // Regresa al menu principal
    @FXML
    void Retroceder(ActionEvent event) {
        ClassBase.RetrocederMenu(Paths.MainMenu, "Menu Memory Core", event);
    }

    // Navega al recuerdo anterior
    @FXML
    private void irAlPasado(ActionEvent event) {
        gestor.irAlPasado();
    }

    // Navega al recuerdo siguiente
    @FXML
    private void irAlFuturo(ActionEvent event) {
        gestor.irAlFuturo();
    }

    // Busca recuerdos por nivel de importancia
    @FXML
    private void buscarPorImportancia(ActionEvent event) {
        try {
            // Verifica que el campo no este vacio
            if (txtBuscarImportancia.getText().isEmpty())
                return;

            // Convierte el valor a entero
            int importancia = Integer.parseInt(txtBuscarImportancia.getText());

            // Obtiene los recuerdos con esa importancia
            ListaDobleRecuerdos resultado = gestor.buscarPorImportancia(importancia);

            // Carga los resultados en la lista
            cargarListaDesde(resultado);

        } catch (NumberFormatException e) {
            // Mensaje de error si el valor no es numerico
            txtRecuerdo.setText("Ingrese un numero valido.");
        }
    }

    // Busca recuerdos por categoria
    @FXML
    private void buscarPorCategoria(ActionEvent event) {

        // Obtiene la categoria seleccionada
        String categoria = cmbBuscarCategoria.getValue();

        // Valida que exista seleccion
        if (categoria == null) {
            txtRecuerdo.setText("Seleccione una categoria.");
            return;
        }

        // Obtiene los recuerdos de la categoria
        ListaDobleRecuerdos resultado = gestor.buscarPorCategoria(categoria);

        // Carga los resultados en la lista
        cargarListaDesde(resultado);
    }

    // Muestra todos los recuerdos almacenados
    @FXML
    private void mostrarTodos() {

        // Obtiene todos los recuerdos del gestor
        ListaDobleRecuerdos todos = gestor.getTodosLosRecuerdos();

        // Carga la lista completa
        cargarListaDesde(todos);
    }

    // Carga una lista doble en el ListView
    private void cargarListaDesde(ListaDobleRecuerdos lista) {

        // Limpia la lista visual
        listaRecuerdos.getItems().clear();

        // Recorre la lista doble
        Nodo<Recuerdo> aux = lista.getCabeza();
        while (aux != null) {

            // Agrega el recuerdo formateado a la lista
            listaRecuerdos.getItems().add(
                    aux.dato.getCategoria() + " | " +
                            aux.dato.getDescripcion() + " | " +
                            "Imp: " + aux.dato.getImportancia()
            );
            aux = aux.siguiente;
        }
    }

    // Metodo llamado cuando cambia el recuerdo actual
    @Override
    public void actualizar(Recuerdo recuerdoActual) {

        // Muestra el recuerdo actual o un mensaje si no existe
        if (recuerdoActual != null) {
            txtRecuerdo.setText(recuerdoActual.toString());
        } else {
            txtRecuerdo.setText("No hay recuerdos en la lista.");
        }

        // Actualiza el area de memoria reciente
        if (areaMemoriaReciente != null) {
            areaMemoriaReciente.setText(moduloReciente.obtenerEstado());
        }
    }
}
