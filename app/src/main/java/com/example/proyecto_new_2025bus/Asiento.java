package com.example.proyecto_new_2025bus;

public class Asiento {
    String nombre;    // Ejemplo: "A1"
    String estado;    // "LIBRE" o "RESERVADO"
    String dniAlumno; // DNI si está reservado, o null si libre

    public Asiento(String nombre) {
        this.nombre = nombre;
        this.estado = "LIBRE";
        this.dniAlumno = null;
    }
}

