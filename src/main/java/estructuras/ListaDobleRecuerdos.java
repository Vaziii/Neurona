package estructuras;

import model.Recuerdo;

public class ListaDobleRecuerdos {

    private Nodo<Recuerdo> cabeza;
    private Nodo<Recuerdo> cola;

    public ListaDobleRecuerdos() {
        cabeza = null;
        cola = null;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public void agregarAlFinal(Recuerdo recuerdo) {
        Nodo<Recuerdo> nuevo = new Nodo<>(recuerdo);
        if (estaVacia()) {
            cabeza = nuevo;
            cola = nuevo;
        } else {
            cola.siguiente = nuevo;
            nuevo.anterior = cola;
            cola = nuevo;
        }
    }

    public Nodo<Recuerdo> getCabeza() {
        return cabeza;
    }

    public Nodo<Recuerdo> getCola() {
        return cola;
    }

    public void eliminarNodo(Nodo<Recuerdo> nodo) {
        if (nodo == null) return;


        if (nodo == cabeza) {
            cabeza = nodo.siguiente;
            if (cabeza != null) cabeza.anterior = null;
        } else if (nodo == cola) {
            cola = nodo.anterior;
            if (cola != null) cola.siguiente = null;
        } else {
            nodo.anterior.siguiente = nodo.siguiente;
            nodo.siguiente.anterior = nodo.anterior;
        }
    }
}
