package estructuras;

public class    ListaSimple<T> {

    private Nodo<T> cabeza;

    public ListaSimple() {
        cabeza = null;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public void agregar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (cabeza == null) {

            cabeza = nuevo;
        } else {
            Nodo<T> aux = cabeza;
            while (aux.siguiente != null) {
                aux = aux.siguiente;
            }
            aux.siguiente = nuevo;
        }
    }

    public boolean eliminar(T dato) {
        if (cabeza == null) return false;

        if (cabeza.dato.equals(dato)) {
            cabeza = cabeza.siguiente;
            return true;
        }

        Nodo<T> aux = cabeza;
        while (aux.siguiente != null) {
            if (aux.siguiente.dato.equals(dato)) {
                aux.siguiente = aux.siguiente.siguiente;
                return true;
            }
            aux = aux.siguiente;
        }
        return false;
    }

    public Nodo<T> getCabeza() {
        return cabeza;
    }
}
