package estructuras;

// Se importa la clase Recuerdo
import model.Recuerdo;

// Clase que implementa una lista doblemente enlazada de recuerdos
public class ListaDobleRecuerdos {

    // Referencia al primer nodo de la lista
    private Nodo<Recuerdo> cabeza;

    // Referencia al ultimo nodo de la lista
    private Nodo<Recuerdo> cola;

    // Constructor que inicializa la lista vacia
    public ListaDobleRecuerdos() {
        cabeza = null;
        cola = null;
    }

    // Metodo que verifica si la lista esta vacia
    public boolean estaVacia() {
        return cabeza == null;
    }

    // Metodo para agregar un recuerdo al final de la lista
    public void agregarAlFinal(Recuerdo recuerdo) {

        // Se crea un nuevo nodo con el recuerdo
        Nodo<Recuerdo> nuevo = new Nodo<>(recuerdo);

        // Si la lista esta vacia, el nodo sera cabeza y cola
        if (estaVacia()) {
            cabeza = nuevo;
            cola = nuevo;
        }
        // Caso contrario, se enlaza al final de la lista
        else {
            cola.siguiente = nuevo;
            nuevo.anterior = cola;
            cola = nuevo;
        }
    }

    // Metodo que retorna la cabeza de la lista
    public Nodo<Recuerdo> getCabeza() {
        return cabeza;
    }

    // Metodo que retorna la cola de la lista
    public Nodo<Recuerdo> getCola() {
        return cola;
    }

    // Metodo que elimina un nodo especifico de la lista
    public void eliminarNodo(Nodo<Recuerdo> nodo) {

        // Si el nodo es nulo, no se realiza ninguna accion
        if (nodo == null)
            return;

        // Si el nodo es la cabeza
        if (nodo == cabeza) {
            cabeza = nodo.siguiente;
            if (cabeza != null)
                cabeza.anterior = null;
        }
        // Si el nodo es la cola
        else if (nodo == cola) {
            cola = nodo.anterior;
            if (cola != null)
                cola.siguiente = null;
        }
        // Si el nodo esta en medio de la lista
        else {
            nodo.anterior.siguiente = nodo.siguiente;
            nodo.siguiente.anterior = nodo.anterior;
        }
    }
}
