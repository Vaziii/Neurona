package sistema;

// Importa la estructura de cola para manejar recuerdos recientes
import estructuras.ColaRecientes;

// Importa la tabla hash para registrar accesos rápidos a recuerdos
import estructuras.TablaHashRecuerdos;

// Importa la clase Recuerdo del modelo
import model.Recuerdo;

// Módulo que gestiona la memoria reciente del sistema
// Implementa la interfaz Modulo para reaccionar a cambios del recuerdo actual
public class ModuloMemoriaReciente implements Modulo {

    // Cola que almacena los recuerdos accedidos recientemente
    private ColaRecientes colaRecientes;

    // Tabla hash que permite acceder rápidamente a los recuerdos recientes
    private TablaHashRecuerdos tablaAccesos;

    // Capacidad máxima de recuerdos recientes que se almacenan
    private static final int CAPACIDAD = 5;

    // Constructor del módulo
    public ModuloMemoriaReciente() {
        // Inicializa la cola con una capacidad fija
        this.colaRecientes = new ColaRecientes(CAPACIDAD);

        // Inicializa la tabla hash con una capacidad determinada
        this.tablaAccesos = new TablaHashRecuerdos(20);
    }

    // Metodo que se ejecuta cada vez que el recuerdo actual cambia
    @Override
    public void actualizar(Recuerdo recuerdoActual) {
        // Verifica que el recuerdo no sea nulo
        if (recuerdoActual != null) {
            // Agrega el recuerdo a la cola de recientes
            colaRecientes.encolar(recuerdoActual);

            // Registra el recuerdo en la tabla hash de accesos
            tablaAccesos.insertar(recuerdoActual);
        }
    }

    // Devuelve un texto con el estado actual de la memoria reciente
    public String obtenerEstado() {
        // Muestra el título y el contenido de la cola de recuerdos recientes
        return "--- Memoria Reciente (Últimos " + CAPACIDAD + ") ---\n"
                + colaRecientes.toString();
    }
}
