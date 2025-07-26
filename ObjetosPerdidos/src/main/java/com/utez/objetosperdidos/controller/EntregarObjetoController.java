package com.utez.objetosperdidos.controller;

import com.utez.objetosperdidos.model.ObjetoPerdido;
import com.utez.objetosperdidos.model.dao.ObjetoDAO;
import com.utez.objetosperdidos.util.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class EntregarObjetoController {

    @FXML private Label lblTitulo;
    @FXML private Label lblDescripcion;
    @FXML private Label lblUbicacion;
    @FXML private Label lblEstado;

    private ObjetoPerdido objeto;
    private final ObjetoDAO dao = new ObjetoDAO();

    public void setObjeto(ObjetoPerdido objeto) {
        this.objeto = objeto;
        lblTitulo.setText(objeto.getNombreObjeto());
        lblDescripcion.setText(objeto.getDescripcion());
        lblUbicacion.setText("📍 " + objeto.getEdificio() + " - Aula " + objeto.getAula());
        lblEstado.setText("Estado actual: " + objeto.getEstado());
    }

    @FXML
    public void confirmarEntrega() {
        int idAlumno = Session.currentUser.getId();  

        boolean ok = dao.marcarComoEntregadoPorAlumno(objeto.getId(), idAlumno);

        Alert alert = new Alert(ok ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(ok ? "Objeto marcado como entregado." : "No se pudo registrar la entrega.");
        alert.showAndWait();

        if (ok) {
            ((Stage) lblTitulo.getScene().getWindow()).close();
        }
    }

    @FXML
    public void cancelar() {
        ((Stage) lblTitulo.getScene().getWindow()).close();
    }
}
