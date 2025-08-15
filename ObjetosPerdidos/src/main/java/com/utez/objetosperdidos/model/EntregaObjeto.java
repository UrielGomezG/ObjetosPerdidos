package com.utez.objetosperdidos.model;

import java.util.Date;

public class EntregaObjeto {
    private long objetoId;
    private String fotoCredencialUrl;
    private Date fechaEntrega;
    private String nombreAlumno;
    private String matricula;

    public EntregaObjeto(long objetoId, String fotoCredencialUrl, Date fechaEntrega,
                         String nombreAlumno, String matricula) {
        this.objetoId = objetoId;
        this.fotoCredencialUrl = fotoCredencialUrl;
        this.fechaEntrega = fechaEntrega;
        this.nombreAlumno = nombreAlumno;
        this.matricula = matricula;
    }

    public long getObjetoId() { return objetoId; }
    public String getFotoCredencialUrl() { return fotoCredencialUrl; }
    public Date getFechaEntrega() { return fechaEntrega; }
    public String getNombreAlumno() { return nombreAlumno; }      // nuevo
    public String getMatricula() { return matricula; }            // nuevo
}
