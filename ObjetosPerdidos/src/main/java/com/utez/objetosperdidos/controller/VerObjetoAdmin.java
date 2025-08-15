package com.utez.objetosperdidos.controller;

import com.utez.objetosperdidos.model.ObjetoPerdido;
import com.utez.objetosperdidos.model.EntregaObjeto;
import com.utez.objetosperdidos.model.dao.EntregarObjetoDao;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.List;

public class VerObjetoAdmin {

    @FXML private Label lblTitulo;
    @FXML private Label lblMarca;
    @FXML private Label lblModelo;
    @FXML private Label lblNoSerie;
    @FXML private Label lblAula;
    @FXML private Label lblEdificio;
    @FXML private Label lblDescripcion;

    @FXML private Label lblAlumno;
    @FXML private Label lblMatricula;
    @FXML private Label lblFechaEntrega;

    @FXML private ImageView imgPreview;
    @FXML private ImageView imgCredencial;

    private ObjetoPerdido objetoActual;
    private final EntregarObjetoDao entregaDao = new EntregarObjetoDao();

    public void initialize() {
        if (objetoActual != null) {
            mostrarDatos();
        }
    }

    public void setObjeto(ObjetoPerdido objeto) {
        this.objetoActual = objeto;
        if (lblTitulo != null) {
            mostrarDatos();
        }
    }

    private void mostrarDatos() {
        // Datos del objeto
        lblTitulo.setText(objetoActual.getNombreObjeto());
        lblMarca.setText(objetoActual.getMarca());
        lblModelo.setText(objetoActual.getModelo());
        lblNoSerie.setText(objetoActual.getNo_serial());
        lblAula.setText(objetoActual.getAula());
        lblEdificio.setText(objetoActual.getEdificio());
        lblDescripcion.setText(objetoActual.getDescripcion());

        // Imagen del objeto
        if (objetoActual.getFotoUrl() != null && !objetoActual.getFotoUrl().isEmpty()) {
            File file = new File(System.getProperty("user.home") + "/Downloads/" + objetoActual.getFotoUrl());
            if (file.exists()) {
                imgPreview.setImage(new Image(file.toURI().toString()));
            }
        }

        // Datos de la entrega
        List<EntregaObjeto> entregas = entregaDao.obtenerEntregaPorObjeto(objetoActual.getId());
        if (!entregas.isEmpty()) {
            EntregaObjeto entrega = entregas.get(entregas.size() - 1);
            lblAlumno.setText(entrega.getNombreAlumno());
            lblMatricula.setText(entrega.getMatricula());
            lblFechaEntrega.setText(entrega.getFechaEntrega() != null ? entrega.getFechaEntrega().toString() : "");

            // Imagen de la credencial
            if (entrega.getFotoCredencialUrl() != null && !entrega.getFotoCredencialUrl().isEmpty()) {
                File credencialFile = new File(System.getProperty("user.home") + "/Downloads/" + entrega.getFotoCredencialUrl());
                if (credencialFile.exists()) {
                    imgCredencial.setImage(new Image(credencialFile.toURI().toString()));
                }
            }
        } else {
            lblAlumno.setText("-");
            lblMatricula.setText("-");
            lblFechaEntrega.setText("-");
        }
    }
}
