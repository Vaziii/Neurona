package controllers;

import estructuras.ArbolABBRecuerdos;
import javafx.event.ActionEvent;
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
import model.Recuerdo;
import sistema.GestorCentralRecuerdos;
import javafx.stage.Stage;
import utilities.ClassBase;
import utilities.Paths;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class VisualizadorArbol {

    @FXML private ScrollPane scrollPane;
    @FXML private Pane panelArbol;
    @FXML private Label lblCategoria;

    private ArbolABBRecuerdos arbolGlobal;
    private String categoriaFiltro;

    private double escala = 1.0;
    private double mouseAnclaX;
    private double mouseAnclaY;

    private static final double RADIO = 30; // Aumenté un poco el radio para que quepa texto
    private static final double ESPACIO_VERTICAL = 100;

    @FXML
    public void initialize() {
        arbolGlobal = GestorCentralRecuerdos.getInstancia().getArbol();
        habilitarZoomYArrastre();
        dibujar();
    }

    public void setCategoria(String categoria) {
        this.categoriaFiltro = categoria;
        if (lblCategoria != null) {
            lblCategoria.setText("Categoría: " + categoria);
        }
        dibujar();
    }

    private void dibujar() {
        if (panelArbol == null) return;
        panelArbol.getChildren().clear();

        if (arbolGlobal == null || arbolGlobal.getRaiz() == null) {
            mensajeVacio("La mente está vacía.");
            return;
        }

        // 1. Recolectar y Ordenar
        List<Recuerdo> listaRecuerdos = new ArrayList<>();
        recolectarNodos(arbolGlobal.getRaiz(), listaRecuerdos, categoriaFiltro);

        if (listaRecuerdos.isEmpty()) {
            mensajeVacio("No hay recuerdos en esta categoría.");
            return;
        }

        listaRecuerdos.sort(Comparator.comparing(Recuerdo::getFechaComoDate));

        // 2. Construir el Árbol Balanceado (Visual)
        ArbolABBRecuerdos.NodoABB raizVisual = construirArbolBalanceado(listaRecuerdos, 0, listaRecuerdos.size() - 1);

        // 3. CÁLCULO DINÁMICO DE ESPACIO (Evita superposición)
        int profundidad = obtenerProfundidad(raizVisual);
        // El ancho base necesario crece exponencialmente con la profundidad
        double unidadAncho = RADIO * 3; // Espacio mínimo entre nodos
        double anchoRequerido = Math.pow(2, profundidad - 1) * unidadAncho;

        // Ajustar el panel para que quepa todo
        panelArbol.setPrefWidth(Math.max(2000, anchoRequerido + 200));

        // Posición inicial X (Centro del ancho calculado)
        double xInicial = panelArbol.getPrefWidth() / 2;

        // El offset inicial debe ser la cuarta parte del ancho total del nivel más bajo
        double offsetInicial = anchoRequerido / 2;

        // 4. Dibujar
        dibujarNodoRecursivo(raizVisual, xInicial, 60, offsetInicial / 2);
    }

    private int obtenerProfundidad(ArbolABBRecuerdos.NodoABB nodo) {
        if (nodo == null) return 0;
        int izq = obtenerProfundidad(nodo.izquierda);
        int der = obtenerProfundidad(nodo.derecha);
        return Math.max(izq, der) + 1;
    }

    private void recolectarNodos(ArbolABBRecuerdos.NodoABB nodo, List<Recuerdo> lista, String categoria) {
        if (nodo == null) return;
        if (categoria == null || nodo.dato.getCategoria().equalsIgnoreCase(categoria)) {
            lista.add(nodo.dato);
        }
        recolectarNodos(nodo.izquierda, lista, categoria);
        recolectarNodos(nodo.derecha, lista, categoria);
    }

    private ArbolABBRecuerdos.NodoABB construirArbolBalanceado(List<Recuerdo> lista, int inicio, int fin) {
        if (inicio > fin) return null;
        int medio = (inicio + fin) / 2;
        ArbolABBRecuerdos.NodoABB nodo = new ArbolABBRecuerdos.NodoABB(lista.get(medio));
        nodo.izquierda = construirArbolBalanceado(lista, inicio, medio - 1);
        nodo.derecha = construirArbolBalanceado(lista, medio + 1, fin);
        return nodo;
    }

    private void dibujarNodoRecursivo(ArbolABBRecuerdos.NodoABB nodo, double x, double y, double offsetH) {
        if (nodo == null) return;

        // Conexiones
        if (nodo.izquierda != null) {
            panelArbol.getChildren().add(new Line(x, y, x - offsetH, y + ESPACIO_VERTICAL));
            dibujarNodoRecursivo(nodo.izquierda, x - offsetH, y + ESPACIO_VERTICAL, offsetH / 2);
        }
        if (nodo.derecha != null) {
            panelArbol.getChildren().add(new Line(x, y, x + offsetH, y + ESPACIO_VERTICAL));
            dibujarNodoRecursivo(nodo.derecha, x + offsetH, y + ESPACIO_VERTICAL, offsetH / 2);
        }

        // Nodo (Círculo)
        Circle circulo = new Circle(x, y, RADIO);
        circulo.setStyle("-fx-fill: #4834d4; -fx-stroke: white; -fx-stroke-width: 2;");

        circulo.setOnMouseClicked(e -> {
            if (lblCategoria != null) lblCategoria.setText("Detalle: " + nodo.dato.getDescripcion());
        });

        // --- CAMBIO AQUÍ: MOSTRAR DESCRIPCIÓN EN LUGAR DE FECHA ---
        String textoMostrar = nodo.dato.getDescripcion();

        // Si la descripción es muy larga (>15 caracteres), la cortamos para que entre en el círculo
        if (textoMostrar.length() > 15) {
            textoMostrar = textoMostrar.substring(0, 12) + "...";
        }

        Text texto = new Text(textoMostrar);
        texto.setStyle("-fx-fill: #000000; ");
        texto.setX(x - (texto.getLayoutBounds().getWidth() / 2));
        texto.setY(y + 4);

        // Texto Importancia
        Text textoImp = new Text("Imp: " + nodo.dato.getImportancia());
        textoImp.setStyle("-fx-fill: #333; -fx-font-size: 9px;");
        textoImp.setX(x - (textoImp.getLayoutBounds().getWidth() / 2));
        textoImp.setY(y + RADIO + 12);

        panelArbol.getChildren().addAll(circulo, texto, textoImp);
    }

    private void mensajeVacio(String msg) {
        Text texto = new Text(50, 50, msg);
        texto.setStyle("-fx-font-size: 20px; -fx-fill: #666;");
        panelArbol.getChildren().add(texto);
    }

    private void habilitarZoomYArrastre() {
        panelArbol.setOnScroll(event -> {
            double factor = (event.getDeltaY() > 0) ? 1.1 : 0.9;
            escala = Math.max(0.1, Math.min(escala * factor, 5.0));
            panelArbol.setScaleX(escala);
            panelArbol.setScaleY(escala);
            event.consume();
        });
        panelArbol.setOnMousePressed(event -> {
            mouseAnclaX = event.getSceneX();
            mouseAnclaY = event.getSceneY();
        });
        panelArbol.setOnMouseDragged(event -> {
            panelArbol.setTranslateX(panelArbol.getTranslateX() + (event.getSceneX() - mouseAnclaX));
            panelArbol.setTranslateY(panelArbol.getTranslateY() + (event.getSceneY() - mouseAnclaY));
            mouseAnclaX = event.getSceneX();
            mouseAnclaY = event.getSceneY();
        });
    }

    @FXML
    private void resetVista() {
        escala = 1.0;
        panelArbol.setScaleX(1.0);
        panelArbol.setScaleY(1.0);
        panelArbol.setTranslateX(0);
        panelArbol.setTranslateY(0);
    }

    @FXML
    private void volver(ActionEvent event) {
        ClassBase.MenuButton(Paths.SeleccionCategoriaView,"Mostrar Arbol por Categorias", event);
    }
}