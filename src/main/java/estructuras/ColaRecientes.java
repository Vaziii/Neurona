package estructuras;
import model.Recuerdo;

public class ColaRecientes {
    private Nodo<Recuerdo> frente;
    private Nodo<Recuerdo> finalCola;
    private int tamano;
    private int capacidadMaxima; 

    public ColaRecientes(int capacidadMaxima) {
        this.frente = null;
        this.finalCola = null;
        this.tamano = 0;
        this.capacidadMaxima = capacidadMaxima;
    }

    public boolean estaVacia() {
        return frente == null;
    }

    public boolean estaSaturada() {
        return tamano >= capacidadMaxima;
    }

    public void encolar(Recuerdo recuerdo) {
        if (estaSaturada()) {
            desencolar();
        }
        
        Nodo<Recuerdo> nuevoNodo = new Nodo<>(recuerdo);
        
        if (estaVacia()) {
            frente = nuevoNodo;
            finalCola = nuevoNodo;
        } else {
            // CAMBIO AQUÍ: Usamos .siguiente directo en vez de setSiguiente()
            finalCola.siguiente = nuevoNodo; 
            finalCola = nuevoNodo;             
        }
        tamano++;
    }

    public Recuerdo desencolar() {
        if (estaVacia()) return null;
        
        // CAMBIO AQUÍ: Usamos .dato directo en vez de getDato()
        Recuerdo dato = frente.dato;
        
        // CAMBIO AQUÍ: Usamos .siguiente directo
        frente = frente.siguiente;
        
        if (frente == null) finalCola = null;
        
        tamano--;
        return dato;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Nodo<Recuerdo> actual = frente;
        int contador = 1;
        while (actual != null) {
            // CAMBIO AQUÍ: Acceso directo a .dato y .siguiente
            sb.append(contador).append(". ")
              .append(actual.dato.getDescripcion())
              .append(" (").append(actual.dato.getFecha()).append(")\n");
            
            actual = actual.siguiente;
            contador++;
        }
        return sb.toString();
    }
}
