package controllers;

// Importa el arbol binario de busqueda de recuerdos
import estructuras.ArbolABBRecuerdos;

// Importaciones de JavaFX para eventos y componentes visuales
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

// Importa el modelo Recuerdo
import model.Recuerdo;

// Importa el gestor central del sistema
import sistema.GestorCentralRecuerdos;

// Importaciones de utilidades de navegacion
import utilities.ClassBase;
import utilities.Paths;

// Importaciones de colecciones
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Controlador encargado de visualizar el arbol de recuerdos
public class VisualizadorArbol {

    // Scroll para permitir desplazamiento del arbol
    @FXML
    private ScrollPane scrollPane;

    // Panel donde se dibuja el arbol
    @FXML
    private Pane panelArbol;

    // Etiqueta para mostrar la categoria o detalles
    @FXML
    private Label lblCategoria;

    // Arbol global de recuerdos
    private ArbolABBRecuerdos arbolGlobal;

    // Categoria usada como filtro
    private String categoriaFiltro;

    // Variables para zoom y arrastre
    private double escala = 1.0;
    private double mouseAnclaX;
    private double mouseAnclaY;

    // Constantes visuales del arbol
    private static final double RADIO = 30;
    private static final double ESPACIO_VERTICAL = 100;

    // Metodo que se ejecuta al inicializar la vista
    @FXML
    public void initialize() {
        // Obtiene el arbol desde el gestor central
        arbolGlobal = GestorCentralRecuerdos.getInstancia().getArbol();

        // Habilita zoom y arrastre con el mouse
        habilitarZoomYArrastre();

        // Dibuja el arbol
        dibujar();
    }

    // Define la categoria a filtrar
    public void setCategoria(String categoria) {
        this.categoriaFiltro = categoria;

        // Actualiza la etiqueta de categoria
        if (lblCategoria != null) {
            lblCategoria.setText("Categoria: " + categoria);
        }

        // Redibuja el arbol
        dibujar();
    }

    // Metodo principal para dibujar el arbol
    private void dibujar() {
        if (panelArbol == null)
            return;

        // Limpia el panel
        panelArbol.getChildren().clear();

        // Verifica si el arbol esta vacio
        if (arbolGlobal == null || arbolGlobal.getRaiz() == null) {
            mensajeVacio("La mente esta vacia.");
            return;
        }

        // Lista para recolectar recuerdos
        List<Recuerdo> listaRecuerdos = new ArrayList<>();

        // Recolecta los nodos segun la categoria
        recolectarNodos(arbolGlobal.getRaiz(), listaRecuerdos, categoriaFiltro);

        // Verifica si no hay recuerdos filtrados
        if (listaRecuerdos.isEmpty()) {
            mensajeVacio("No hay recuerdos en esta categoria.");
            return;
        }

        // Ordena los recuerdos por fecha
        listaRecuerdos.sort(Comparator.comparing(Recuerdo::getFechaComoDate));

        // Construye un arbol balanceado solo para visualizacion
        ArbolABBRecuerdos.NodoABB raizVisual =
                construirArbolBalanceado(listaRecuerdos, 0, listaRecuerdos.size() - 1);

        // Calcula la profundidad del arbol
        int profundidad = obtenerProfundidad(raizVisual);

        // Define el espacio horizontal base
        double unidadAncho = RADIO * 3;

        // Calcula el ancho requerido segun la profundidad
        double anchoRequerido = Math.pow(2, profundidad - 1) * unidadAncho;

        // Ajusta el ancho del panel
        panelArbol.setPrefWidth(Math.max(2000, anchoRequerido + 200));

        // Posicion inicial centrada
        double xInicial = panelArbol.getPrefWidth() / 2;

        // Offset horizontal inicial
        double offsetInicial = anchoRequerido / 2;

        // Dibuja el arbol de forma recursiva
        dibujarNodoRecursivo(raizVisual, xInicial, 60, offsetInicial / 2);
    }

    // Calcula la profundidad del arbol
    private int obtenerProfundidad(ArbolABBRecuerdos.NodoABB nodo) {
        if (nodo == null)
            return 0;

        int izq = obtenerProfundidad(nodo.izquierda);
        int der = obtenerProfundidad(nodo.derecha);

        return Math.max(izq, der) + 1;
    }

    // Recolecta los recuerdos del arbol segun la categoria
    private void recolectarNodos(ArbolABBRecuerdos.NodoABB nodo, List<Recuerdo> lista, String categoria) {
        if (nodo == null)
            return;

        // Agrega el recuerdo si cumple el filtro
        if (categoria == null || nodo.dato.getCategoria().equalsIgnoreCase(categoria)) {
            lista.add(nodo.dato);
        }

        // Recorre subarbol izquierdo y derecho
        recolectarNodos(nodo.izquierda, lista, categoria);
        recolectarNodos(nodo.derecha, lista, categoria);
    }

    // Construye un arbol balanceado a partir de una lista ordenada
    private ArbolABBRecuerdos.NodoABB construirArbolBalanceado(List<Recuerdo> lista, int inicio, int fin) {
        if (inicio > fin)
            return null;

        int medio = (inicio + fin) / 2;

        ArbolABBRecuerdos.NodoABB nodo =
                new ArbolABBRecuerdos.NodoABB(lista.get(medio));

        nodo.izquierda = construirArbolBalanceado(lista, inicio, medio - 1);
        nodo.derecha = construirArbolBalanceado(lista, medio + 1, fin);

        return nodo;
    }

    // Dibuja cada nodo y sus conexiones
    private void dibujarNodoRecursivo(ArbolABBRecuerdos.NodoABB nodo, double x, double y, double offsetH) {
        if (nodo == null)
            return;

        // Dibuja conexion izquierda
        if (nodo.izquierda != null) {
            panelArbol.getChildren().add(
                    new Line(x, y, x - offsetH, y + ESPACIO_VERTICAL)
            );
            dibujarNodoRecursivo(nodo.izquierda, x - offsetH, y + ESPACIO_VERTICAL, offsetH / 2);
        }

        // Dibuja conexion derecha
        if (nodo.derecha != null) {
            panelArbol.getChildren().add(
                    new Line(x, y, x + offsetH, y + ESPACIO_VERTICAL)
            );
            dibujarNodoRecursivo(nodo.derecha, x + offsetH, y + ESPACIO_VERTICAL, offsetH / 2);
        }

        // Crea el circulo del nodo
        Circle circulo = new Circle(x, y, RADIO);
        circulo.setStyle("-fx-fill: #4834d4; -fx-stroke: white; -fx-stroke-width: 2;");

        // Evento al hacer clic en el nodo
        circulo.setOnMouseClicked(e -> {
            if (lblCategoria != null)
                lblCategoria.setText("Detalle: " + nodo.dato.getDescripcion());
        });

        // Texto a mostrar dentro del nodo
        String textoMostrar = nodo.dato.getDescripcion();

        // Acorta el texto si es muy largo
        if (textoMostrar.length() > 15) {
            textoMostrar = textoMostrar.substring(0, 12) + "...";
        }

        // Texto principal del nodo
        Text texto = new Text(textoMostrar);
        texto.setStyle("-fx-fill: #000000;");
        texto.setX(x - (texto.getLayoutBounds().getWidth() / 2));
        texto.setY(y + 4);

        // Texto de importancia
        Text textoImp = new Text("Imp: " + nodo.dato.getImportancia());
        textoImp.setStyle("-fx-fill: #333; -fx-font-size: 9px;");
        textoImp.setX(x - (textoImp.getLayoutBounds().getWidth() / 2));
        textoImp.setY(y + RADIO + 12);

        // Agrega los elementos al panel
        panelArbol.getChildren().addAll(circulo, texto, textoImp);
    }

    // Muestra un mensaje cuando no hay datos
    private void mensajeVacio(String msg) {
        Text texto = new Text(50, 50, msg);
        texto.setStyle("-fx-font-size: 20px; -fx-fill: #666;");
        panelArbol.getChildren().add(texto);
    }

    // Habilita zoom con scroll y arrastre con mouse
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

    // Restaura la vista original
    @FXML
    private void resetVista() {
        escala = 1.0;
        panelArbol.setScaleX(1.0);
        panelArbol.setScaleY(1.0);
        panelArbol.setTranslateX(0);
        panelArbol.setTranslateY(0);
    }

    // Regresa a la pantalla anterior
    @FXML
    private void volver(ActionEvent event) {
        ClassBase.MenuButton(Paths.SeleccionCategoriaView,
                "Mostrar Arbol por Categorias", event);
    }
}
