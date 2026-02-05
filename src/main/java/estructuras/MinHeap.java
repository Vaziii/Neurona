package estructuras;

import model.Recuerdo;

// Clase que implementa un Min Heap de recuerdos basado en importancia
public class MinHeap {

    // Arreglo que representa el heap
    private Recuerdo[] heap;

    // Cantidad actual de elementos en el heap
    private int tamano;

    // Capacidad maxima del heap
    private int capacidad;

    // Constructor que inicializa el heap con una capacidad fija
    public MinHeap(int capacidad) {
        this.capacidad = capacidad;
        this.tamano = 0;
        this.heap = new Recuerdo[capacidad];
    }

    // Metodo para insertar un recuerdo en el heap
    public void insertar(Recuerdo recuerdo) {

        // Si el heap esta lleno, no se inserta
        if (tamano >= capacidad)
            return;

        // Se inserta el recuerdo al final del arreglo
        heap[tamano] = recuerdo;
        int actual = tamano;

        // Se reordena el heap hacia arriba segun la importancia
        while (actual > 0 &&
                heap[actual].getImportancia() < heap[padre(actual)].getImportancia()) {

            intercambiar(actual, padre(actual));
            actual = padre(actual);
        }

        // Se incrementa el tamano del heap
        tamano++;
    }

    // Metodo que extrae y retorna el recuerdo con menor importancia
    public Recuerdo extraerMinimo() {

        // Si el heap esta vacio
        if (tamano <= 0)
            return null;

        // Si solo hay un elemento
        if (tamano == 1) {
            tamano--;
            return heap[0];
        }

        // Se guarda la raiz del heap
        Recuerdo raiz = heap[0];

        // Se mueve el ultimo elemento a la raiz
        heap[0] = heap[tamano - 1];
        tamano--;

        // Se reordena el heap hacia abajo
        minHeapify(0);

        return raiz;
    }

    // Metodo que elimina el recuerdo de menor importancia segun una categoria
    public Recuerdo eliminarPorCategoria(String categoria) {

        int indiceMin = -1;
        int minImp = Integer.MAX_VALUE;

        // Se busca el recuerdo con menor importancia de la categoria indicada
        for (int i = 0; i < tamano; i++) {
            if (heap[i].getCategoria().equalsIgnoreCase(categoria)) {
                if (heap[i].getImportancia() < minImp) {
                    minImp = heap[i].getImportancia();
                    indiceMin = i;
                }
            }
        }

        // Si no se encontro la categoria
        if (indiceMin == -1)
            return null;

        // Se guarda el recuerdo eliminado
        Recuerdo removido = heap[indiceMin];

        // Se reemplaza por el ultimo elemento
        heap[indiceMin] = heap[tamano - 1];
        tamano--;

        // Se reordena el heap si es necesario
        if (indiceMin < tamano) {
            minHeapify(indiceMin);
            int actual = indiceMin;
            while (actual > 0 &&
                    heap[actual].getImportancia() < heap[padre(actual)].getImportancia()) {

                intercambiar(actual, padre(actual));
                actual = padre(actual);
            }
        }

        return removido;
    }

    // Metodo que mantiene la propiedad del min heap desde una posicion
    private void minHeapify(int pos) {

        int izq = izquierda(pos);
        int der = derecha(pos);
        int menor = pos;

        if (izq < tamano &&
                heap[izq].getImportancia() < heap[menor].getImportancia())
            menor = izq;

        if (der < tamano &&
                heap[der].getImportancia() < heap[menor].getImportancia())
            menor = der;

        if (menor != pos) {
            intercambiar(pos, menor);
            minHeapify(menor);
        }
    }

    // Metodo para intercambiar dos posiciones del heap
    private void intercambiar(int p1, int p2) {
        Recuerdo tmp = heap[p1];
        heap[p1] = heap[p2];
        heap[p2] = tmp;
    }

    // Metodo que retorna el indice del padre
    private int padre(int pos) {
        return (pos - 1) / 2;
    }

    // Metodo que retorna el indice del hijo izquierdo
    private int izquierda(int pos) {
        return (2 * pos) + 1;
    }

    // Metodo que retorna el indice del hijo derecho
    private int derecha(int pos) {
        return (2 * pos) + 2;
    }

    // Metodo que verifica si el heap esta vacio
    public boolean estaVacio() {
        return tamano == 0;
    }
}
