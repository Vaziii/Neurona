package model;

import java.time.LocalDate;

public class Recuerdo {

    private String descripcion;
    private int importancia;
    private String fecha;
    private String categoria;

    public Recuerdo(String descripcion, int importancia, String fecha, String categoria) {
        this.descripcion = descripcion;
        this.importancia = importancia;
        this.fecha = fecha;
        this.categoria = categoria;

    }

    public LocalDate getFechaComoDate() {
        return LocalDate.parse(fecha);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getImportancia() {
        return importancia;
    }

    public String getFecha() {
        return fecha;
    }

    public String getCategoria() {
        return categoria;
    }

    @Override
    public String toString() {
        return "Descripción: " + descripcion +
                "\nImportancia: " + importancia +
                "\nFecha: " + fecha +
                "\nCategoría: " + categoria;
    }

    public String toFileString() {
        return descripcion + " | " + importancia + " | " + fecha + "\n";
    }
}
