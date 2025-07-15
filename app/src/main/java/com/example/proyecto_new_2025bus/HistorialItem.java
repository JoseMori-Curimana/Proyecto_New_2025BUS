package com.example.proyecto_new_2025bus;

public class HistorialItem {
    private String dni;
    private String nombre;
    private String fecha;
    private String estado;
    private String asiento;

    public HistorialItem(String dni, String nombre, String fecha, String estado, String asiento) {
        this.dni = dni;
        this.nombre = nombre;
        this.fecha = fecha;
        this.estado = estado;
        this.asiento = asiento;
    }

    public String getDni() { return dni; }
    public String getNombre() { return nombre; }
    public String getFecha() { return fecha; }
    public String getEstado() { return estado; }
    public String getAsiento() { return asiento; }
}

