package com.utez.objetosperdidos.model;

import java.util.Date;

public class EntregaObjeto {
    private long objetoId;
    private String fotoCredencialUrl;
    private Date fechaEntrega;

    public EntregaObjeto(long objetoId, String fotoCredencialUrl, Date fechaEntrega) {
        this.objetoId = objetoId;
        this.fotoCredencialUrl = fotoCredencialUrl;
        this.fechaEntrega = fechaEntrega;
    }

    public long getObjetoId() {
        return objetoId;
    }

    public String getFotoCredencialUrl() {
        return fotoCredencialUrl;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }
}
