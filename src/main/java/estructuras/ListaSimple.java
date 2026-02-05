package estructuras;

// Clase generica que implementa una lista simplemente enlazada
public class ListaSimple<T> {

    // Referencia al primer nodo de la lista
    private Nodo<T> cabeza;

    // Constructor que inicializa la lista vacia
    public ListaSimple() {
        cabeza = null;
    }

    // Metodo que verifica si la lista esta vacia
    public boolean estaVacia() {
        return cabeza == null;
    }

    // Metodo para agregar un dato al final de la lista
    public void agregar(T dato) {

        // Se crea un nuevo nodo con el dato
        Nodo<T> nuevo = new Nodo<>(dato);

        // Si la lista esta vacia, el nuevo nodo sera la cabeza
        if (cabeza == null) {
            cabeza = nuevo;
        }
        // Caso contrario, se recorre la lista hasta el ultimo nodo
        else {
            Nodo<T> aux = cabeza;
            while (aux.siguiente != null) {
                aux = aux.siguiente;
            }
            // Se enlaza el nuevo nodo al final
            aux.siguiente = nuevo;
        }
    }

    // Metodo para eliminar un dato de la lista
    public boolean eliminar(T dato) {

        // Si la lista esta vacia, no se puede eliminar
        if (cabeza == null)
            return false;

        // Si el dato a eliminar esta en la cabeza
        if (cabeza.dato.equals(dato)) {
            cabeza = cabeza.siguiente;
            return true;
        }

        // Nodo auxiliar para recorrer la lista
        Nodo<T> aux = cabeza;

        // Se busca el nodo anterior al que se desea eliminar
        while (aux.siguiente != null) {
            if (aux.siguiente.dato.equals(dato)) {
                aux.siguiente = aux.siguiente.siguiente;
                return true;
            }
            aux = aux.siguiente;
        }

        // Si el dato no se encuentra en la lista
        return false;
    }

    // Metodo que retorna la cabeza de la lista
    public Nodo<T> getCabeza() {
        return cabeza;
    }
}
