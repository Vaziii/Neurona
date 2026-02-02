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

    @FXML
    private Button IdMenu;

    @FXML
    private TextField txtDescripcion;
    @FXML
    private TextField txtImportancia;
    @FXML
    private TextField txtFecha;
    @FXML
    private TextField txtBuscarImportancia;

    @FXML
    private ComboBox<String> cmbBuscarCategoria;

    @FXML
    private ListView<String> listaRecuerdos;
    @FXML
    private TextArea txtRecuerdo;
    @FXML
    private TextArea areaMemoriaReciente;

    private GestorCentralRecuerdos gestor;
    private ModuloMemoriaReciente moduloReciente;

    // ================= INICIALIZACIÓN =================
    @FXML
    public void initialize() {
        gestor = new GestorCentralRecuerdos();
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

    // ================= AGREGAR =================
    @FXML
    private void agregarRecuerdo(ActionEvent event) {
        try {
            String categoria = cmbBuscarCategoria.getValue();
            String descripcion = txtDescripcion.getText();
            int importancia = Integer.parseInt(txtImportancia.getText());
            String fecha = txtFecha.getText();

            if (txtImportancia.getText().isEmpty()) {
                 txtRecuerdo.setText("Ingrese la importancia.");
                 return;
            }
            

            if (categoria == null || descripcion.isEmpty() || fecha.isEmpty()) {
                txtRecuerdo.setText("Complete todos los campos.");
                return;
            }

            if (importancia < 1 || importancia > 10) {
                txtRecuerdo.setText("La importancia debe estar entre 1 y 10.");
                return;
            }

            Recuerdo nuevo = new Recuerdo(descripcion, importancia, fecha, categoria);
            boolean agregado = gestor.agregarRecuerdo(nuevo);

            if (!agregado) {
                txtRecuerdo.setText("Límite de 30 recuerdos alcanzado para esta categoría.");
                return;
            }


            limpiarCampos();
            mostrarTodos();

        } catch (NumberFormatException e) {
            txtRecuerdo.setText("La importancia debe ser un número.");
        }
    }

    private void limpiarCampos() {
        txtDescripcion.clear();
        txtImportancia.clear();
        txtFecha.clear();
        cmbBuscarCategoria.setValue(null);
    }

    // ================= NAVEGACIÓN =================
    @FXML
    private void irAlPasado(ActionEvent event) {
        gestor.irAlPasado();}

    @FXML
    private void irAlFuturo(ActionEvent event) {
        gestor.irAlFuturo();
    }

    // ================= BÚSQUEDA =================
    @FXML
    private void buscarPorImportancia(ActionEvent event) {
        try {
            int importancia = Integer.parseInt(txtBuscarImportancia.getText());
            ListaDobleRecuerdos resultado = gestor.buscarPorImportancia(importancia);
            cargarListaDesde(resultado);
        } catch (NumberFormatException e) {
            txtRecuerdo.setText("Ingrese un número válido para la importancia.");
        }
    }

    @FXML
    private void buscarPorCategoria(ActionEvent event) {
        String categoria = cmbBuscarCategoria.getValue();

        if (categoria == null || categoria.isEmpty()) {
            txtRecuerdo.setText("Seleccione una categoría para buscar.");
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
                            aux.dato.getImportancia()
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
            txtRecuerdo.setText("No hay recuerdos almacenados.");
        }
        if (areaMemoriaReciente != null) {
            areaMemoriaReciente.setText(moduloReciente.obtenerEstado());
        }
    }
}
