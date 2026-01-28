package sistema;

import estructuras.ListaDobleRecuerdos;
import estructuras.ListaSimple;
import estructuras.Nodo;
import model.Recuerdo;

import java.io.*;

public class GestorCentralRecuerdos {

    private ListaDobleRecuerdos recuerdos;
    private Nodo<Recuerdo> actual;
    private ListaSimple<Modulo> modulos;

    public GestorCentralRecuerdos() {
        recuerdos = new ListaDobleRecuerdos();
        actual = null;
        modulos = new ListaSimple<>();
        cargarDesdeArchivos();
    }

    // ================= ALMACENAMIENTO =================
    public boolean agregarRecuerdo(Recuerdo recuerdo) {
        if (contarPorCategoria(recuerdo.getCategoria()) >= 30) {
            return false;
        }

        recuerdos.agregarAlFinal(recuerdo);
        guardarEnArchivo(recuerdo);


        if (actual == null) {
            actual = recuerdos.getCabeza();
        }

        notificarModulos();
        return true;
    }

    public Recuerdo getRecuerdoActual() {
        return actual != null ? actual.dato : null;
    }

    // ================= RECORRIDO =================
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

    // ================= LISTAS =================
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

    // ================= SINCRONIZACIÓN =================
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

    // ================= ARCHIVOS =================
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
                        recuerdos.agregarAlFinal(new Recuerdo(descripcion, importancia, fecha, categoria));
                    }
                }
            } catch (IOException e) {
                System.out.println("Error leyendo archivo: " + archivo.getName());
            }
        }

        actual = recuerdos.getCabeza();
    }
    // ================= BÚSQUEDA =================
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

        // Si la categoría se quedó vacía, elimina el archivo físico .txt
        if (!tieneRecuerdos && archivo.exists()) {
            archivo.delete();
        }
    }
}