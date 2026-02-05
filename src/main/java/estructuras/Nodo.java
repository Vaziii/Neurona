package estructuras;

// Clase generica que representa un nodo para estructuras enlazadas
public class Nodo<T> {

    // Dato almacenado en el nodo
    public T dato;

    // Referencia al siguiente nodo
    public Nodo<T> siguiente;

    // Referencia al nodo anterior
    public Nodo<T> anterior;

    // Constructor que inicializa el nodo con un dato
    public Nodo(T dato) {

        // Se asigna el dato al nodo
        this.dato = dato;

        // Inicialmente no apunta a ningun otro nodo
        this.siguiente = null;
        this.anterior = null;
    }
}
