package com.utez.objetosperdidos.model;

public class ObjetoPerdido {
    private int id;
    private String nombreObjeto;
    private String descripcion;
    private String fotoUrl;
    private String aula;
    private String edificio;
    private String estado;

    public ObjetoPerdido() {
    }

    public ObjetoPerdido(int id, String nombreObjeto, String descripcion, String fotoUrl, String aula, String edificio, String estado) {
        this.id = id;
        this.nombreObjeto = nombreObjeto;
        this.descripcion = descripcion;
        this.fotoUrl = fotoUrl;
        this.aula = aula;
        this.edificio = edificio;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreObjeto() {
        return nombreObjeto;
    }

    public void setNombreObjeto(String nombreObjeto) {
        this.nombreObjeto = nombreObjeto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
