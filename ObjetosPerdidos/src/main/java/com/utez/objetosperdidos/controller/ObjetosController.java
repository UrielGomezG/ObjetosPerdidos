package com.utez.objetosperdidos.controller;

import com.utez.objetosperdidos.model.ObjetoPerdido;
import com.utez.objetosperdidos.model.dao.ObjetoDAO;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class ObjetosController {

    @FXML
    private FlowPane contenedorTarjetas;

    @FXML
    public void initialize() {
        ObjetoDAO dao = new ObjetoDAO();
        List<ObjetoPerdido> objetos = dao.obtenerObjetosConInfo(0, 20);

        for (ObjetoPerdido obj : objetos) {
            VBox tarjeta = crearTarjetaBasica(obj);
            contenedorTarjetas.getChildren().add(tarjeta);
        }
    }

    private VBox crearTarjetaBasica(ObjetoPerdido obj) {
        VBox tarjeta = new VBox(5);
        tarjeta.setPadding(new Insets(10));
        tarjeta.setStyle("-fx-border-color: #ccc; -fx-background-color: #eee;");
        tarjeta.setPrefWidth(200);

        Label lblNombre = new Label("📦 " + obj.getNombreObjeto());
        Label lblDescripcion = new Label(obj.getDescripcion());
        Label lblUbicacion = new Label("📍 " + obj.getEdificio() + " - Aula " + obj.getAula());
        Label lblEstado = new Label("Estado: " + obj.getEstado());

        lblDescripcion.setWrapText(true);

        tarjeta.getChildren().addAll(lblNombre, lblDescripcion, lblUbicacion, lblEstado);

        return tarjeta;
    }
}
