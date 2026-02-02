package estructuras;

import model.Recuerdo;

public class TablaHashRecuerdos {
    private ListaSimple<Recuerdo>[] tabla;
    private int capacidad;

    @SuppressWarnings("unchecked")
    public TablaHashRecuerdos(int capacidad) {
        this.capacidad = capacidad;
        this.tabla = new ListaSimple[capacidad];
        for (int i = 0; i < capacidad; i++) {
            tabla[i] = new ListaSimple<>();
        }
    }

    private int funcionHash(String clave) {
        int hash = 0;
        for (int i = 0; i < clave.length(); i++) {
            hash += clave.charAt(i);
        }
        return Math.abs(hash % capacidad);
    }

    public void insertar(Recuerdo recuerdo) {
        int indice = funcionHash(recuerdo.getDescripcion());
        tabla[indice].agregar(recuerdo);
    }

    public Recuerdo buscar(String descripcion) {
        int indice = funcionHash(descripcion);
        ListaSimple<Recuerdo> lista = tabla[indice];
        
        // Asumiendo que ListaSimple tiene un método getCabeza() que devuelve el Nodo
        // Si ListaSimple no tiene getCabeza(), avísame.
        Nodo<Recuerdo> actual = lista.getCabeza(); 
        
        while (actual != null) {
            // CAMBIO AQUÍ: Usamos .dato y .siguiente directo
            if (actual.dato.getDescripcion().equals(descripcion)) {
                return actual.dato;
            }
            actual = actual.siguiente;
        }
        return null; 
    }
}
