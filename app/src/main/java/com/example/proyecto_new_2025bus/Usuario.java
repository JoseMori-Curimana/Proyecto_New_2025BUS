package com.example.proyecto_new_2025bus;

public class Usuario {

    private String codigo;
    private String password;
    private String nombre;
    private String rol;

    // Constructor sin el campo 'asignadoABus' ya que no se usa más
    public Usuario(String codigo, String password, String nombre, String rol) {
        this.codigo = codigo;
        this.password = password;
        this.nombre = nombre;
        this.rol = rol;
    }

    // Getters y Setters
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
