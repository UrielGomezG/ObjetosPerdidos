package com.utez.objetosperdidos.controller;

import com.utez.objetosperdidos.model.ObjetoPerdido;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class VerObjeto {

    @FXML
    private Label lblTitulo;
    @FXML
    private Label lblMarca;
    @FXML
    private Label lblModelo;
    @FXML
    private Label lblNoSerial;
    @FXML
    private Label lblAula;
    @FXML
    private Label lblEdificio;
    @FXML
    private Label lblCategoria;
    @FXML
    private Label lblDescripcion;

    @FXML
    private ImageView imgObjeto;

    private ObjetoPerdido objetoActual;

    private void initialize() {
        // Si el objeto ya fue asignado antes, mostramos los datos
        if (objetoActual != null) {
            mostrarDatos();
        }
    }

    public void setObjeto(ObjetoPerdido objeto) {
        this.objetoActual = objeto;
        // Solo llamar mostrarDatos si los @FXML ya están inicializados
        if (lblTitulo != null) {
            mostrarDatos();
        }
    }

    private void mostrarDatos() {
        lblTitulo.setText(objetoActual.getNombreObjeto());
        lblMarca.setText(objetoActual.getMarca());
        lblModelo.setText(objetoActual.getModelo());
        lblNoSerial.setText(objetoActual.getNo_serial());
        lblAula.setText(objetoActual.getAula());
        lblEdificio.setText(objetoActual.getEdificio());
        lblCategoria.setText(objetoActual.getCategoria() != null ? objetoActual.getCategoria().getNombre() : "");
        lblDescripcion.setText(objetoActual.getDescripcion());

        // Cargar imagen
        if (objetoActual.getFotoUrl() != null && !objetoActual.getFotoUrl().isEmpty()) {
            File file = new File(System.getProperty("user.home") + "/Downloads/" + objetoActual.getFotoUrl());
            if (file.exists()) {
                imgObjeto.setImage(new Image(file.toURI().toString()));
            }
        }
    }
}
