package estructuras;

// Se importa la clase Recuerdo
import model.Recuerdo;

// Clase que implementa una tabla hash para almacenar recuerdos
public class TablaHashRecuerdos {

    // Arreglo de listas simples para manejar colisiones
    private ListaSimple<Recuerdo>[] tabla;

    // Capacidad de la tabla hash
    private int capacidad;

    // Constructor que inicializa la tabla hash
    @SuppressWarnings("unchecked")
    public TablaHashRecuerdos(int capacidad) {

        this.capacidad = capacidad;

        // Se crea el arreglo de listas
        this.tabla = new ListaSimple[capacidad];

        // Se inicializa cada posicion con una lista vacia
        for (int i = 0; i < capacidad; i++) {
            tabla[i] = new ListaSimple<>();
        }
    }

    // Funcion hash basada en la suma de los valores ASCII de la clave
    private int funcionHash(String clave) {

        int hash = 0;

        // Se recorren los caracteres de la clave
        for (int i = 0; i < clave.length(); i++) {
            hash += clave.charAt(i);
        }

        // Se obtiene un indice valido dentro de la tabla
        return Math.abs(hash % capacidad);
    }

    // Metodo para insertar un recuerdo en la tabla hash
    public void insertar(Recuerdo recuerdo) {

        // Se obtiene el indice usando la funcion hash
        int indice = funcionHash(recuerdo.getDescripcion());

        // Se agrega el recuerdo en la lista correspondiente
        tabla[indice].agregar(recuerdo);
    }

    // Metodo para buscar un recuerdo por su descripcion
    public Recuerdo buscar(String descripcion) {

        // Se obtiene el indice de la tabla
        int indice = funcionHash(descripcion);

        // Se obtiene la lista en ese indice
        ListaSimple<Recuerdo> lista = tabla[indice];

        // Nodo auxiliar para recorrer la lista
        Nodo<Recuerdo> actual = lista.getCabeza();

        // Se recorre la lista buscando la descripcion
        while (actual != null) {
            if (actual.dato.getDescripcion().equals(descripcion)) {
                return actual.dato;
            }
            actual = actual.siguiente;
        }

        // Si no se encuentra el recuerdo
        return null;
    }
}
