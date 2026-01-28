package estructuras;

import model.Recuerdo;

public class MinHeap {
    private Recuerdo[] heap;
    private int tamano;
    private int capacidad;

    public MinHeap(int capacidad) {
        this.capacidad = capacidad;
        this.tamano = 0;
        this.heap = new Recuerdo[capacidad];
    }

    public void insertar(Recuerdo recuerdo) {
        if (tamano >= capacidad) return;
        heap[tamano] = recuerdo;
        int actual = tamano;
        while (actual > 0 && heap[actual].getImportancia() < heap[padre(actual)].getImportancia()) {
            intercambiar(actual, padre(actual));
            actual = padre(actual);
        }
        tamano++;
    }

    public Recuerdo extraerMinimo() {
        if (tamano <= 0) return null;
        if (tamano == 1) {
            tamano--;
            return heap[0];
        }
        Recuerdo raiz = heap[0];
        heap[0] = heap[tamano - 1];
        tamano--;
        minHeapify(0);
        return raiz;
    }

    private void minHeapify(int pos) {
        int izq = izquierda(pos);
        int der = derecha(pos);
        int menor = pos;

        if (izq < tamano && heap[izq].getImportancia() < heap[menor].getImportancia()) {
            menor = izq;
        }
        if (der < tamano && heap[der].getImportancia() < heap[menor].getImportancia()) {
            menor = der;
        }
        if (menor != pos) {
            intercambiar(pos, menor);
            minHeapify(menor);
        }
    }

    private int padre(int pos) { return (pos - 1) / 2; }
    private int izquierda(int pos) { return (2 * pos) + 1; }
    private int derecha(int pos) { return (2 * pos) + 2; }

    private void intercambiar(int pos1, int pos2) {
        Recuerdo tmp = heap[pos1];
        heap[pos1] = heap[pos2];

        heap[pos2] = tmp;
    }

    public boolean estaVacio() { return tamano == 0; }
}