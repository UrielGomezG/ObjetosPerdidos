package com.utez.objetosperdidos.controller;

import com.utez.objetosperdidos.model.ObjetoPerdido;
import com.utez.objetosperdidos.model.dao.ObjetoDAO;
import com.utez.objetosperdidos.util.Session;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EntregarObjetoController {

    @FXML
    private Label lblTitulo;
    @FXML
    private Label lblMarca;
    @FXML
    private Label lblModelo;
    @FXML
    private Label lblNoSerie;
    @FXML
    private Label lblDescripcion;
    @FXML
    private TextField txtAlumno;
    @FXML
    private TextField txtMatricula;

    private ObjetoPerdido objeto;
    private final ObjetoDAO dao = new ObjetoDAO();

    public void setObjeto(ObjetoPerdido objeto) {
        this.objeto = objeto;
        lblTitulo.setText(objeto.getNombreObjeto());
        lblMarca.setText(objeto.getMarca());
        lblModelo.setText(objeto.getModelo());
        lblNoSerie.setText(objeto.getNo_serial());
        lblDescripcion.setText(objeto.getDescripcion());
    }

    @FXML
    public void confirmarEntrega() {
        // Validar que se ingresen los datos del alumno
        if (txtAlumno.getText().trim().isEmpty() || txtMatricula.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Por favor, complete todos los campos del alumno.");
            alert.showAndWait();
            return;
        }

        // Aquí podrías agregar lógica adicional para validar la matrícula
        // Por ahora usamos el ID del usuario actual como administrador que registra la entrega
        int idAdmin = Session.currentUser.getId();

        boolean ok = dao.marcarComoEntregadoPorAlumno(objeto.getId(), idAdmin);

        Alert alert = new Alert(ok ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(ok ? "Objeto marcado como entregado al alumno: " + txtAlumno.getText() : "No se pudo registrar la entrega.");
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
