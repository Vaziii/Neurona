package estructuras;

// Se importa la clase Recuerdo
import model.Recuerdo;

// Clase que implementa una Cola de recuerdos recientes
public class ColaRecientes {

    // Referencia al primer nodo de la cola
    private Nodo<Recuerdo> frente;

    // Referencia al último nodo de la cola
    private Nodo<Recuerdo> finalCola;

    // Número actual de elementos en la cola
    private int tamano;

    // Capacidad máxima permitida en la cola
    private int capacidadMaxima;

    // Constructor que inicializa la cola con una capacidad máxima
    public ColaRecientes(int capacidadMaxima) {
        this.frente = null;          // La cola inicia vacía
        this.finalCola = null;
        this.tamano = 0;
        this.capacidadMaxima = capacidadMaxima;
    }

    // Metodo que verifica si la cola está vacía
    public boolean estaVacia() {
        return frente == null;
    }

    // Metodo que verifica si la cola alcanzó su capacidad máxima
    public boolean estaSaturada() {
        return tamano >= capacidadMaxima;
    }

    // Metodo para insertar un recuerdo en la cola
    public void encolar(Recuerdo recuerdo) {

        // Si la cola está llena, se elimina el elemento más antiguo
        if (estaSaturada()) {
            desencolar();
        }

        // Se crea un nuevo nodo con el recuerdo
        Nodo<Recuerdo> nuevoNodo = new Nodo<>(recuerdo);

        // Si la cola está vacía, el nuevo nodo será el frente y el final
        if (estaVacia()) {
            frente = nuevoNodo;
            finalCola = nuevoNodo;
        }
        // Caso contrario, se inserta al final de la cola
        else {
            finalCola.siguiente = nuevoNodo;
            finalCola = nuevoNodo;
        }

        // Se incrementa el tamaño de la cola
        tamano++;
    }

    // Metodo para eliminar y devolver el elemento del frente de la cola
    public Recuerdo desencolar() {

        // Si la cola está vacía, no hay nada que eliminar
        if (estaVacia())
            return null;

        // Se guarda el dato del frente
        Recuerdo dato = frente.dato;

        // El frente avanza al siguiente nodo
        frente = frente.siguiente;

        // Si la cola queda vacía, el final también se establece en null
        if (frente == null)
            finalCola = null;

        // Se decrementa el tamaño de la cola
        tamano--;

        // Se retorna el recuerdo eliminado
        return dato;
    }

    // Metodo que devuelve una representación en texto de la cola
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        // Nodo auxiliar para recorrer la cola
        Nodo<Recuerdo> actual = frente;
        int contador = 1;

        // Recorre la cola desde el frente hasta el final
        while (actual != null) {

            // Se agrega la información del recuerdo al texto
            sb.append(contador).append(". ")
                    .append(actual.dato.getDescripcion())
                    .append(" (").append(actual.dato.getFecha()).append(")\n");

            actual = actual.siguiente;
            contador++;
        }

        return sb.toString();
    }
}
