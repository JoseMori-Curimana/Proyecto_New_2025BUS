package com.example.proyecto_new_2025bus;

public class Chofer {
    private String id;
    private String nombre;
    private String dni;
    private String telefono;

    public Chofer() {} // Constructor vacío requerido por Firestore

    public Chofer(String id, String nombre, String dni, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.dni = dni;
        this.telefono = telefono;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDni() {
        return dni;
    }

    public String getTelefono() {
        return telefono;
    }
}
