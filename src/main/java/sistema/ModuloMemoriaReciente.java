package sistema;

import estructuras.ColaRecientes;
import estructuras.TablaHashRecuerdos;
import model.Recuerdo;

public class ModuloMemoriaReciente implements Modulo {
    
    private ColaRecientes colaRecientes;
    private TablaHashRecuerdos tablaAccesos;
    private static final int CAPACIDAD = 5; // Guardamos los últimos 5

    public ModuloMemoriaReciente() {
        this.colaRecientes = new ColaRecientes(CAPACIDAD);
        this.tablaAccesos = new TablaHashRecuerdos(20);
    }

    @Override
    public void actualizar(Recuerdo recuerdoActual) {
        if (recuerdoActual != null) {
            // 1. Meter a la cola 
            colaRecientes.encolar(recuerdoActual);
            // 2. Meter al hash
            tablaAccesos.insertar(recuerdoActual);
        }
    }
    
    // Este método lo usará la pantalla para mostrar el texto
    public String obtenerEstado() {
        return "--- Memoria Reciente (Últimos " + CAPACIDAD + ") ---\n" + colaRecientes.toString();
    }
}
