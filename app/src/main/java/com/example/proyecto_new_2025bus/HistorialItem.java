package com.example.proyecto_new_2025bus;

public class HistorialItem {
    private String titulo;
    private String fecha;
    private String detalle;

    public HistorialItem(String titulo, String fecha, String detalle) {
        this.titulo = titulo;
        this.fecha = fecha;
        this.detalle = detalle;
    }

    public String getTitulo() { return titulo; }
    public String getFecha() { return fecha; }
    public String getDetalle() { return detalle; }
}
