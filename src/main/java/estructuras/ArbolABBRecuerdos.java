package estructuras;

import model.Recuerdo;

public class ArbolABBRecuerdos {

    public static class NodoABB {
        public Recuerdo dato;
        public NodoABB izquierda;
        public NodoABB derecha;

        public NodoABB(Recuerdo dato) {
            this.dato = dato;
        }
    }

    private NodoABB raiz;

    public NodoABB getRaiz() {
        return raiz;
    }

    public void insertar(Recuerdo recuerdo) {
        raiz = insertarRec(raiz, recuerdo);
    }

    private NodoABB insertarRec(NodoABB actual, Recuerdo recuerdo) {
        if (actual == null) {
            return new NodoABB(recuerdo);
        }

        // 🔹 Conversión CORRECTA desde String → LocalDate
        if (recuerdo.getFechaComoDate()
                .isBefore(actual.dato.getFechaComoDate())) {

            actual.izquierda = insertarRec(actual.izquierda, recuerdo);
        } else {
            actual.derecha = insertarRec(actual.derecha, recuerdo);
        }
        return actual;
    }
}

