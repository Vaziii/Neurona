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

    /**
     * Busca y elimina el recuerdo de menor importancia de una categoría específica.
     */
    public Recuerdo eliminarPorCategoria(String categoria) {
        int indiceMin = -1;
        int minImp = Integer.MAX_VALUE;

        for (int i = 0; i < tamano; i++) {
            if (heap[i].getCategoria().equalsIgnoreCase(categoria)) {
                if (heap[i].getImportancia() < minImp) {
                    minImp = heap[i].getImportancia();
                    indiceMin = i;
                }
            }
        }

        if (indiceMin == -1) return null;

        Recuerdo removido = heap[indiceMin];
        heap[indiceMin] = heap[tamano - 1];
        tamano--;

        if (indiceMin < tamano) {
            minHeapify(indiceMin);
            int actual = indiceMin;
            while (actual > 0 && heap[actual].getImportancia() < heap[padre(actual)].getImportancia()) {
                intercambiar(actual, padre(actual));
                actual = padre(actual);
            }
        }
        return removido;
    }

    private void minHeapify(int pos) {
        int izq = izquierda(pos);
        int der = derecha(pos);
        int menor = pos;
        if (izq < tamano && heap[izq].getImportancia() < heap[menor].getImportancia()) menor = izq;
        if (der < tamano && heap[der].getImportancia() < heap[menor].getImportancia()) menor = der;
        if (menor != pos) {
            intercambiar(pos, menor);
            minHeapify(menor);
        }
    }

    private void intercambiar(int p1, int p2) {
        Recuerdo tmp = heap[p1];
        heap[p1] = heap[p2];
        heap[p2] = tmp;
    }

    private int padre(int pos) { return (pos - 1) / 2; }
    private int izquierda(int pos) { return (2 * pos) + 1; }
    private int derecha(int pos) { return (2 * pos) + 2; }
    public boolean estaVacio() { return tamano == 0; }
}