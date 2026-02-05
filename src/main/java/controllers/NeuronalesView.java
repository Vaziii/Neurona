package controllers;

import estructuras.ListaDobleRecuerdos;
import estructuras.Nodo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Recuerdo;
import sistema.GestorCentralRecuerdos;
import sistema.Modulo;
import sistema.ModuloMemoriaReciente;
import utilities.ClassBase;
import utilities.Paths;

public class NeuronalesView implements Modulo {

    @FXML private Button IdMenu;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtImportancia;
    @FXML private TextField txtFecha;
    @FXML private TextField txtBuscarImportancia;
    @FXML private ComboBox<String> cmbBuscarCategoria;
    @FXML private ListView<String> listaRecuerdos;
    @FXML private TextArea txtRecuerdo;
    @FXML private TextArea areaMemoriaReciente;

    private GestorCentralRecuerdos gestor;
    private ModuloMemoriaReciente moduloReciente;

    // ================= INICIALIZACIÓN =================
    @FXML
    public void initialize() {
        gestor = GestorCentralRecuerdos.getInstancia();
        moduloReciente = new ModuloMemoriaReciente();
        gestor.registrarModulo(moduloReciente);
        gestor.registrarModulo(this);

        cmbBuscarCategoria.getItems().addAll(
                "Chisme", "Sueños", "Familia", "Salidas",
                "Estudio", "Trabajo", "Otros"
        );

        mostrarTodos();
    }

    // ================= MENÚ =================
    @FXML
    void Retroceder(ActionEvent event) {
        ClassBase.RetrocederMenu(Paths.MainMenu, "Menu Memory Core", event);
    }

    // ================= NAVEGACIÓN =================
    @FXML
    private void irAlPasado(ActionEvent event) {
        gestor.irAlPasado();
    }

    @FXML
    private void irAlFuturo(ActionEvent event) {
        gestor.irAlFuturo();
    }

    // ================= BÚSQUEDA =================
    @FXML
    private void buscarPorImportancia(ActionEvent event) {
        try {
            if (txtBuscarImportancia.getText().isEmpty()) return;
            int importancia = Integer.parseInt(txtBuscarImportancia.getText());
            ListaDobleRecuerdos resultado = gestor.buscarPorImportancia(importancia);
            cargarListaDesde(resultado);
        } catch (NumberFormatException e) {
            txtRecuerdo.setText("Ingrese un número válido.");
        }
    }

    @FXML
    private void buscarPorCategoria(ActionEvent event) {
        String categoria = cmbBuscarCategoria.getValue();
        if (categoria == null) {
            txtRecuerdo.setText("Seleccione una categoría.");
            return;
        }
        ListaDobleRecuerdos resultado = gestor.buscarPorCategoria(categoria);
        cargarListaDesde(resultado);
    }

    @FXML
    private void mostrarTodos() {
        ListaDobleRecuerdos todos = gestor.getTodosLosRecuerdos();
        cargarListaDesde(todos);
    }

    private void cargarListaDesde(ListaDobleRecuerdos lista) {
        listaRecuerdos.getItems().clear();
        Nodo<Recuerdo> aux = lista.getCabeza();
        while (aux != null) {
            listaRecuerdos.getItems().add(
                    aux.dato.getCategoria() + " | " +
                            aux.dato.getDescripcion() + " | " +
                            "Imp: " + aux.dato.getImportancia()
            );
            aux = aux.siguiente;
        }
    }

    // ================= SINCRONIZACIÓN =================
    @Override
    public void actualizar(Recuerdo recuerdoActual) {
        if (recuerdoActual != null) {
            txtRecuerdo.setText(recuerdoActual.toString());
        } else {
            txtRecuerdo.setText("No hay recuerdos en la lista.");
        }

        if (areaMemoriaReciente != null) {
            areaMemoriaReciente.setText(moduloReciente.obtenerEstado());
        }
    }
}