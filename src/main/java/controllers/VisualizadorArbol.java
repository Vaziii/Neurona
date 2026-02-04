package controllers;

import estructuras.ArbolABBRecuerdos;
import estructuras.ArbolABBRecuerdos.NodoABB;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import model.Recuerdo;

public class VisualizadorArbol {

    @FXML
    private Pane panelArbol;

    private ArbolABBRecuerdos arbol;

    @FXML
    public void initialize() {
        arbol = new ArbolABBRecuerdos();
        dibujar(); // árbol inicia vacío
    }

    /**
     * Este método es llamado desde IngresoController
     * cada vez que el usuario crea un nuevo Recuerdo.
     */
    public void agregarRecuerdo(Recuerdo recuerdo) {
        if (recuerdo == null) return;
        arbol.insertar(recuerdo);
        dibujar();
    }

    private void dibujar() {
        panelArbol.getChildren().clear();

        if (arbol.getRaiz() != null) {
            dibujarNodo(arbol.getRaiz(), panelArbol.getWidth() / 2, 40, 200);
        }
    }

    private void dibujarNodo(NodoABB nodo, double x, double y, double separacion) {
        if (nodo == null) return;

        // Nodo
        Circle circulo = new Circle(x, y, 20);
        circulo.setFill(Color.LIGHTBLUE);
        circulo.setStroke(Color.BLACK);

        // Texto (fecha)
        Text texto = new Text(x - 30, y + 5, nodo.dato.getFecha());

        panelArbol.getChildren().addAll(circulo, texto);

        // Subárbol izquierdo
        if (nodo.izquierda != null) {
            Line lineaIzq = new Line(x, y + 20, x - separacion, y + 80);
            panelArbol.getChildren().add(lineaIzq);
            dibujarNodo(nodo.izquierda, x - separacion, y + 80, separacion / 2);
        }

        // Subárbol derecho
        if (nodo.derecha != null) {
            Line lineaDer = new Line(x, y + 20, x + separacion, y + 80);
            panelArbol.getChildren().add(lineaDer);
            dibujarNodo(nodo.derecha, x + separacion, y + 80, separacion / 2);
        }
    }
}

