package controllers;

import estructuras.ArbolABBRecuerdos;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import sistema.GestorCentralRecuerdos;
import javafx.stage.Stage;

public class VisualizadorArbol {

    @FXML private ScrollPane scrollPane;
    @FXML private Pane panelArbol;
    @FXML private Label lblCategoria;

    private ArbolABBRecuerdos arbol;
    private String categoriaFiltro;
    private double escala = 1.0;
    private double mouseAnclaX;
    private double mouseAnclaY;

    private static final double RADIO = 20;
    private static final double ESPACIO_HORIZONTAL = 80;
    private static final double ESPACIO_VERTICAL = 80;

    @FXML
    public void initialize() {
        GestorCentralRecuerdos gestor = GestorCentralRecuerdos.getInstancia();
        arbol = gestor.getArbol();

        dibujar();
        habilitarZoomYArrastre();
    }

    public void setCategoria(String categoria) {
        this.categoriaFiltro = categoria;
        if (lblCategoria != null) {
            lblCategoria.setText("Categoría: " + categoria);
        }
        dibujar();
    }

    private void dibujar() {
        panelArbol.getChildren().clear();
        if (arbol != null && arbol.getRaiz() != null) {
            dibujarNodo(arbol.getRaiz(), 600, 50, 250);
        }
    }

    private void dibujarNodo(ArbolABBRecuerdos.NodoABB nodo, double x, double y, double separacion) {
        if (nodo == null) return;

        Circle circulo = new Circle(x, y, RADIO);
        circulo.setStyle("-fx-fill: #6A5ACD; -fx-stroke: black;");

        Text texto = new Text(nodo.dato.getDescripcion());
        texto.setX(x - texto.getLayoutBounds().getWidth() / 2);
        texto.setY(y + 5);

        panelArbol.getChildren().addAll(circulo, texto);

        if (nodo.izquierda != null) {
            double nuevoX = x - separacion;
            double nuevoY = y + ESPACIO_VERTICAL;

            Line linea = new Line(x, y + RADIO, nuevoX, nuevoY - RADIO);
            panelArbol.getChildren().add(linea);

            dibujarNodo(nodo.izquierda, nuevoX, nuevoY, separacion / 1.5);
        }

        if (nodo.derecha != null) {
            double nuevoX = x + separacion;
            double nuevoY = y + ESPACIO_VERTICAL;

            Line linea = new Line(x, y + RADIO, nuevoX, nuevoY - RADIO);
            panelArbol.getChildren().add(linea);

            dibujarNodo(nodo.derecha, nuevoX, nuevoY, separacion / 1.5);
        }
    }

    private void habilitarZoomYArrastre() {

        panelArbol.setOnScroll(event -> {
            double factor = (event.getDeltaY() > 0) ? 1.1 : 0.9;
            escala *= factor;
            panelArbol.setScaleX(escala);
            panelArbol.setScaleY(escala);
            event.consume();
        });

        panelArbol.setOnMousePressed(event -> {
            mouseAnclaX = event.getSceneX();
            mouseAnclaY = event.getSceneY();
        });

        panelArbol.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - mouseAnclaX;
            double deltaY = event.getSceneY() - mouseAnclaY;

            panelArbol.setTranslateX(panelArbol.getTranslateX() + deltaX);
            panelArbol.setTranslateY(panelArbol.getTranslateY() + deltaY);

            mouseAnclaX = event.getSceneX();
            mouseAnclaY = event.getSceneY();
        });
    }

    @FXML
    private void volver() {
        Stage stage = (Stage) panelArbol.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void resetVista() {
        escala = 1.0;
        panelArbol.setScaleX(escala);
        panelArbol.setScaleY(escala);
        panelArbol.setTranslateX(0);
        panelArbol.setTranslateY(0);
    }

    @FXML
    private void volverMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) panelArbol.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Viajes Neuronales");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
