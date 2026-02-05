package sistema;

import estructuras.ListaDobleRecuerdos;
import estructuras.ListaSimple;
import estructuras.MinHeap; // IMPORTADO
import estructuras.Nodo;
import model.Recuerdo;
import estructuras.ArbolABBRecuerdos;
import java.io.File;

import java.io.*;

public class GestorCentralRecuerdos {

    private ListaDobleRecuerdos recuerdos;
    private Nodo<Recuerdo> actual;
    private ListaSimple<Modulo> modulos;
    private static GestorCentralRecuerdos instancia;
    private ArbolABBRecuerdos arbol;


    public GestorCentralRecuerdos() {
        recuerdos = new ListaDobleRecuerdos();
        actual = null;
        modulos = new ListaSimple<>();
        arbol = new ArbolABBRecuerdos();
        cargarDesdeArchivos();
    }

    public static GestorCentralRecuerdos getInstancia() {
        if (instancia == null) {
            instancia = new GestorCentralRecuerdos();
        }
        return instancia;
    }


    // Almacenamiento con Olvido Inteligente Automático
    public String agregarRecuerdo(Recuerdo recuerdo) {
        // Validar formato de fecha
        try {
            recuerdo.getFechaComoDate();
        } catch (Exception e) {
            return "Error: La fecha debe estar en formato YYYY-MM-DD (ejemplo: 2025-02-04).";
        }

        String mensaje = "Recuerdo guardado correctamente.";


        // Si la categoría ya tiene 30 o más, aplicamos el heap para borrar el de menor importancia
        if (contarPorCategoria(recuerdo.getCategoria()) >= 30) {

            // 1. Creamos un MinHeap para la categoría llena
            MinHeap heapPrioridad = new MinHeap(35);
            Nodo<Recuerdo> aux = recuerdos.getCabeza();

            // 2. Filtramos los recuerdos de esa categoría y los metemos al Heap
            while (aux != null) {
                if (aux.dato.getCategoria().equalsIgnoreCase(recuerdo.getCategoria())) {
                    heapPrioridad.insertar(aux.dato);
                }
                aux = aux.siguiente;
            }

            // 3. Extraemos el de menor importancia
            Recuerdo paraEliminar = heapPrioridad.extraerMinimo();

            // 4. Lo borramos de la memoria y del archivo físico
            if (paraEliminar != null) {
                eliminarRecuerdo(paraEliminar);
                // Actualizamos el mensaje para avisar al usuario
                mensaje = "Olvido inteligente: Se eliminó [" + paraEliminar.getDescripcion() + "] por falta de espacio.";
            }
        }

        // Ahora que hay espacio (máximo 29), agregamos el nuevo
        recuerdos.agregarAlFinal(recuerdo);
        guardarEnArchivo(recuerdo);

        if (actual == null) {
            actual = recuerdos.getCabeza();
        }

        notificarModulos();
        arbol.insertar(recuerdo);

        // Retornamos el reporte de lo sucedido
        return mensaje;
    }

    public Recuerdo getRecuerdoActual() {
        return actual != null ? actual.dato : null;
    }

    //Recorrido
    public void irAlPasado() {
        if (actual != null && actual.anterior != null) {
            actual = actual.anterior;
            notificarModulos();
        }
    }

    public void irAlFuturo() {
        if (actual != null && actual.siguiente != null) {
            actual = actual.siguiente;
            notificarModulos();
        }
    }

    // Lista
    public ListaDobleRecuerdos getTodosLosRecuerdos() {
        return recuerdos;
    }

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

    // Sincronizacion
    public void registrarModulo(Modulo modulo) {
        modulos.agregar(modulo);
    }

    private void notificarModulos() {
        Recuerdo recuerdoActual = getRecuerdoActual();
        Nodo<Modulo> aux = modulos.getCabeza();
        while (aux != null) {
            aux.dato.actualizar(recuerdoActual);
            aux = aux.siguiente;
        }
    }

    // Archivos
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

    // Busqueda
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

    public void eliminarRecuerdo(Recuerdo recuerdo) {
        // 1. Eliminar de la lista doble en memoria
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

        // 2. Actualizar o borrar el archivo físico
        actualizarArchivoCategoria(recuerdo.getCategoria());
        notificarModulos();
    }

    private void actualizarArchivoCategoria(String categoria) {
        File carpeta = new File("recuerdos");
        File archivo = new File(carpeta, categoria + ".txt");
        boolean tieneRecuerdos = false;

        try (FileWriter writer = new FileWriter(archivo, false)) { // false para sobrescribir
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

        if (!tieneRecuerdos && archivo.exists()) {
            archivo.delete();
        }
    }

    public ArbolABBRecuerdos getArbol() {
        return arbol;
    }

    public ListaSimple<String> getCategorias() {
        ListaSimple<String> categorias = new ListaSimple<>();

        File carpeta = new File("recuerdos"); // ajusta si tu ruta es distinta
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