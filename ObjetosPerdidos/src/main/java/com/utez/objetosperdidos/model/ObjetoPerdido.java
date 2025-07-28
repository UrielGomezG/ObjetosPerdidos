package com.utez.objetosperdidos.model;

public class ObjetoPerdido {
    private int id;
    private String nombreObjeto;
    private String descripcion;
    private String fotoUrl;
    private String aula;
    private String edificio;  
    private String estado;
    private String marca;
    private String modelo;
    private String no_serial;     
    private int edificioId;   
    private int estadoId;      

    public ObjetoPerdido() {
    }

public ObjetoPerdido(int id, String nombreObjeto, String descripcion, String fotoUrl, String aula,
                     String edificio, String estado,
                     String marca, String modelo, String no_serial) {
    this.id = id;
    this.nombreObjeto = nombreObjeto;
    this.descripcion = descripcion;
    this.fotoUrl = fotoUrl;
    this.aula = aula;
    this.edificio = edificio;
    this.estado = estado;
    this.marca = marca;
    this.modelo = modelo;
    this.no_serial = no_serial;
}


    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getNo_serial() {
        return no_serial;
    }

    public void setNo_serial(String no_serial) {
        this.no_serial = no_serial;
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

    public int getEdificioId() {
        return edificioId;
    }

    public void setEdificioId(int edificioId) {
        this.edificioId = edificioId;
    }

    public int getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(int estadoId) {
        this.estadoId = estadoId;
    }
}
