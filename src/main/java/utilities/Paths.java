package utilities;

/**
 * Clase utilitaria que centraliza las rutas de los archivos FXML y recursos.
 *
 * Evita escribir rutas "quemadas" en los controladores y facilita el
 * mantenimiento del proyecto.
 */
public class Paths {

    // Vista principal del módulo de viajes neuronales
    public static final String MainView = "/NeuronalesView.fxml";

    // Menú principal de la aplicación
    public static final String MainMenu = "/MainMenu.fxml";

    // Vista para ingresar un nuevo recuerdo
    public static final String IngresoView = "/IngresoView.fxml";

    // Vista del módulo de olvido de recuerdos
    public static final String OlvidoView = "/OlvidoView.fxml";

    // Hoja de estilos CSS de la aplicación
    public static final String CSS = "/estilos.css";

    // Vista para seleccionar la categoría antes de mostrar el árbol
    public static final String SeleccionCategoriaView = "/SeleccionCategoria.fxml";

    // Vista que muestra el árbol de recuerdos por categoría
    public static final String VisualizadorArbolView = "/VisualizadorArbol.fxml";
}
