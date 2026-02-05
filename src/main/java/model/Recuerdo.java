package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// Clase que representa un recuerdo con descripcion, importancia, fecha y categoria
public class Recuerdo {

    // Atributos del recuerdo
    private String descripcion;
    private int importancia;
    private String fecha;
    private String categoria;

    // Constructor que inicializa todos los atributos del recuerdo
    public Recuerdo(String descripcion, int importancia, String fecha, String categoria) {
        this.descripcion = descripcion;
        this.importancia = importancia;
        this.fecha = fecha;
        this.categoria = categoria;
    }

    // Metodo que convierte la fecha en formato String a LocalDate
    public LocalDate getFechaComoDate() {

        // Se define el formato de la fecha
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Se convierte la fecha a LocalDate
        // Lanza una excepcion si la fecha es invalida
        return LocalDate.parse(fecha, formatter);
    }

    // Metodo que retorna la descripcion del recuerdo
    public String getDescripcion() {
        return descripcion;
    }

    // Metodo que retorna la importancia del recuerdo
    public int getImportancia() {
        return importancia;
    }

    // Metodo que retorna la fecha del recuerdo
    public String getFecha() {
        return fecha;
    }

    // Metodo que retorna la categoria del recuerdo
    public String getCategoria() {
        return categoria;
    }

    // Metodo que devuelve una representacion en texto del recuerdo
    @Override
    public String toString() {
        return "Descripción: " + descripcion +
                "\nImportancia: " + importancia +
                "\nFecha: " + fecha +
                "\nCategoría: " + categoria;
    }

    // Metodo que devuelve una cadena para guardar el recuerdo en un archivo
    public String toFileString() {
        return descripcion + " | " + importancia + " | " + fecha + "\n";
    }
}
