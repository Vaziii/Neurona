package sistema;

import estructuras.ListaDobleRecuerdos;
import estructuras.ListaSimple;
import estructuras.MinHeap;
import estructuras.Nodo;
import model.Recuerdo;
import estructuras.ArbolABBRecuerdos;
import java.io.File;
import java.io.*;

// Clase central que gestiona todos los recuerdos del sistema
public class GestorCentralRecuerdos {

    // Lista doble para navegar recuerdos en pasado y futuro
    private ListaDobleRecuerdos recuerdos;

    // Nodo que apunta al recuerdo actual
    private Nodo<Recuerdo> actual;

    // Lista de modulos observadores
    private ListaSimple<Modulo> modulos;

    // Instancia unica del gestor (patron Singleton)
    private static GestorCentralRecuerdos instancia;

    // Arbol ABB para organizar recuerdos por fecha
    private ArbolABBRecuerdos arbol;

    // Constructor privado que inicializa las estructuras
    public GestorCentralRecuerdos() {
        recuerdos = new ListaDobleRecuerdos();
        actual = null;
        modulos = new ListaSimple<>();
        arbol = new ArbolABBRecuerdos();
        cargarDesdeArchivos();
    }

    // Metodo para obtener la unica instancia del gestor
    public static GestorCentralRecuerdos getInstancia() {
        if (instancia == null) {
            instancia = new GestorCentralRecuerdos();
        }
        return instancia;
    }

    // Metodo para agregar un nuevo recuerdo al sistema
    public String agregarRecuerdo(Recuerdo recuerdo) {

        // Se valida el formato de la fecha
        try {
            recuerdo.getFechaComoDate();
        } catch (Exception e) {
            return "Error: La fecha debe estar en formato YYYY-MM-DD (ejemplo: 2025-02-04).";
        }

        String mensaje = "Recuerdo guardado correctamente.";

        // Si hay demasiados recuerdos de la misma categoria
        if (contarPorCategoria(recuerdo.getCategoria()) >= 30) {

            // Se usa un MinHeap para eliminar el menos importante
            MinHeap heapPrioridad = new MinHeap(35);
            Nodo<Recuerdo> aux = recuerdos.getCabeza();

            while (aux != null) {
                if (aux.dato.getCategoria().equalsIgnoreCase(recuerdo.getCategoria())) {
                    heapPrioridad.insertar(aux.dato);
                }
                aux = aux.siguiente;
            }

            // Se elimina el recuerdo menos importante
            Recuerdo paraEliminar = heapPrioridad.extraerMinimo();

            if (paraEliminar != null) {
                eliminarRecuerdo(paraEliminar);
                mensaje = "Olvido inteligente: Se elimino [" +
                        paraEliminar.getDescripcion() +
                        "] por falta de espacio.";
            }
        }

        // Se agrega el recuerdo a la lista principal
        recuerdos.agregarAlFinal(recuerdo);

        // Se guarda en archivo
        guardarEnArchivo(recuerdo);

        // Se inicializa el recuerdo actual
        if (actual == null) {
            actual = recuerdos.getCabeza();
        }

        // Se notifica a los modulos observadores
        notificarModulos();

        // Se inserta en el arbol ABB
        arbol.insertar(recuerdo);

        return mensaje;
    }

    // Retorna el recuerdo actual
    public Recuerdo getRecuerdoActual() {
        return actual != null ? actual.dato : null;
    }

    // Mueve el puntero al recuerdo anterior
    public void irAlPasado() {
        if (actual != null && actual.anterior != null) {
            actual = actual.anterior;
            notificarModulos();
        }
    }

    // Mueve el puntero al recuerdo siguiente
    public void irAlFuturo() {
        if (actual != null && actual.siguiente != null) {
            actual = actual.siguiente;
            notificarModulos();
        }
    }

    // Retorna todos los recuerdos
    public ListaDobleRecuerdos getTodosLosRecuerdos() {
        return recuerdos;
    }

    // Busca recuerdos por categoria
    public ListaDobleRecuerdos buscarPorCategoria(String categoria) {
        ListaDobleRecuerdos resultado = new ListaDobleRecuerdos();
        Nodo<Recuerdo> aux = recuerdos.getCabeza();

        while (aux != null) {
            if (aux.dato.getCategoria().equalsIgnoreCase(categoria)) {
                resultado.agregarAlFinal(aux.dato);
            }
            aux = aux.siguiente;
        }

        return resultado;
    }

    // Cuenta recuerdos por categoria
    private int contarPorCategoria(String categoria) {
        int contador = 0;
        Nodo<Recuerdo> aux = recuerdos.getCabeza();

        while (aux != null) {
            if (aux.dato.getCategoria().equalsIgnoreCase(categoria)) {
                contador++;
            }
            aux = aux.siguiente;
        }

        return contador;
    }

    // Registra un modulo observador
    public void registrarModulo(Modulo modulo) {
        modulos.agregar(modulo);
    }

    // Notifica a todos los modulos
    private void notificarModulos() {
        Recuerdo recuerdoActual = getRecuerdoActual();
        Nodo<Modulo> aux = modulos.getCabeza();

        while (aux != null) {
            aux.dato.actualizar(recuerdoActual);
            aux = aux.siguiente;
        }
    }

    // Guarda un recuerdo en archivo segun su categoria
    private void guardarEnArchivo(Recuerdo recuerdo) {
        try {
            File carpeta = new File("recuerdos");
            if (!carpeta.exists()) carpeta.mkdirs();

            File archivo = new File(carpeta, recuerdo.getCategoria() + ".txt");
            FileWriter writer = new FileWriter(archivo, true);
            writer.write(recuerdo.toFileString());
            writer.close();

        } catch (IOException e) {
            System.out.println("Error al guardar recuerdo: " + e.getMessage());
        }
    }

    // Carga recuerdos desde los archivos al iniciar el sistema
    private void cargarDesdeArchivos() {
        File carpeta = new File("recuerdos");
        if (!carpeta.exists()) return;

        File[] archivos = carpeta.listFiles();
        if (archivos == null) return;

        for (File archivo : archivos) {
            String categoria = archivo.getName().replace(".txt", "");
            try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    String[] partes = linea.split(" \\| ");
                    if (partes.length == 3) {
                        String descripcion = partes[0];
                        int importancia = Integer.parseInt(partes[1]);
                        String fecha = partes[2];

                        Recuerdo recuerdo = new Recuerdo(descripcion, importancia, fecha, categoria);
                        recuerdos.agregarAlFinal(recuerdo);
                        arbol.insertar(recuerdo);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error leyendo archivo: " + archivo.getName());
            }
        }

        actual = recuerdos.getCabeza();
    }

    // Busca recuerdos por importancia
    public ListaDobleRecuerdos buscarPorImportancia(int importancia) {
        ListaDobleRecuerdos resultado = new ListaDobleRecuerdos();
        Nodo<Recuerdo> aux = recuerdos.getCabeza();

        while (aux != null) {
            if (aux.dato.getImportancia() == importancia) {
                resultado.agregarAlFinal(aux.dato);
            }
            aux = aux.siguiente;
        }

        return resultado;
    }

    // Elimina un recuerdo del sistema
    public void eliminarRecuerdo(Recuerdo recuerdo) {
        Nodo<Recuerdo> aux = recuerdos.getCabeza();

        while (aux != null) {
            if (aux.dato.getDescripcion().equals(recuerdo.getDescripcion()) &&
                    aux.dato.getFecha().equals(recuerdo.getFecha())) {

                recuerdos.eliminarNodo(aux);
                if (actual == aux) actual = recuerdos.getCabeza();
                break;
            }
            aux = aux.siguiente;
        }

        actualizarArchivoCategoria(recuerdo.getCategoria());
        notificarModulos();
    }

    // Actualiza el archivo de una categoria especifica
    private void actualizarArchivoCategoria(String categoria) {
        File carpeta = new File("recuerdos");
        File archivo = new File(carpeta, categoria + ".txt");
        boolean tieneRecuerdos = false;

        try (FileWriter writer = new FileWriter(archivo, false)) {
            Nodo<Recuerdo> aux = recuerdos.getCabeza();
            while (aux != null) {
                if (aux.dato.getCategoria().equalsIgnoreCase(categoria)) {
                    writer.write(aux.dato.toFileString());
                    tieneRecuerdos = true;
                }
                aux = aux.siguiente;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Si ya no hay recuerdos, se elimina el archivo
        if (!tieneRecuerdos && archivo.exists()) {
            archivo.delete();
        }
    }

    // Retorna el arbol ABB de recuerdos
    public ArbolABBRecuerdos getArbol() {
        return arbol;
    }

    // Obtiene las categorias existentes desde los archivos
    public ListaSimple<String> getCategorias() {
        ListaSimple<String> categorias = new ListaSimple<>();

        File carpeta = new File("recuerdos");
        if (carpeta.exists() && carpeta.isDirectory()) {
            File[] archivos = carpeta.listFiles((dir, name) -> name.endsWith(".txt"));
            if (archivos != null) {
                for (File f : archivos) {
                    String nombre = f.getName().replace(".txt", "");
                    categorias.agregar(nombre);
                }
            }
        }

        return categorias;
    }
}
