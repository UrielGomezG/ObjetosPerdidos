package com.utez.objetosperdidos.model;

public class Edificio {
    private int id;
    private String nombre;

    public Edificio(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
