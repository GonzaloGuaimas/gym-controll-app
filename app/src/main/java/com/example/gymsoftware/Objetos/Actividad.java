package com.example.gymsoftware.Objetos;

public class Actividad {
    private String nombre;
    private Integer precio;
    private long id;

    public Actividad(String nombre, Integer precio) {
        this.nombre = nombre;
        this.precio = precio;
    }
    public Actividad(String nombre, Integer precio, long id) {
        this.nombre = nombre;
        this.precio = precio;
        this.id = id;
    }
    public Actividad(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }
}
