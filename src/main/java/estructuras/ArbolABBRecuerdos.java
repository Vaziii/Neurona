package estructuras;

// Se importa la clase Recuerdo desde el paquete model
import model.Recuerdo;

// Clase que implementa un Árbol Binario de Búsqueda (ABB)
// donde los recuerdos se ordenan por fecha
public class ArbolABBRecuerdos {

    // Clase interna que representa un nodo del árbol
    public static class NodoABB {
        public Recuerdo dato;        // Dato almacenado en el nodo
        public NodoABB izquierda;    // Referencia al hijo izquierdo
        public NodoABB derecha;      // Referencia al hijo derecho

        // Constructor del nodo
        public NodoABB(Recuerdo dato) {
            this.dato = dato;        // Se asigna el recuerdo al nodo
        }
    }

    // Raíz del árbol
    private NodoABB raiz;

    // Metodo que retorna la raíz del árbol
    public NodoABB getRaiz() {
        return raiz;
    }

    // Metodo público para insertar un recuerdo en el árbol
    public void insertar(Recuerdo recuerdo) {
        // Llama al metodo recursivo comenzando desde la raíz
        raiz = insertarRec(raiz, recuerdo);
    }

    // Metodo recursivo que inserta un recuerdo en el ABB
    private NodoABB insertarRec(NodoABB actual, Recuerdo recuerdo) {

        // Caso base: si el nodo actual es nulo,
        // se crea un nuevo nodo con el recuerdo
        if (actual == null) {
            return new NodoABB(recuerdo);
        }

        // Si la fecha del nuevo recuerdo es anterior
        // a la fecha del nodo actual, se inserta a la izquierda
        if (recuerdo.getFechaComoDate()
                .isBefore(actual.dato.getFechaComoDate())) {

            actual.izquierda = insertarRec(actual.izquierda, recuerdo);
        }
        // Caso contrario, se inserta a la derecha
        else {
            actual.derecha = insertarRec(actual.derecha, recuerdo);
        }

        // Se retorna el nodo actual para mantener el enlace del árbol
        return actual;
    }
}