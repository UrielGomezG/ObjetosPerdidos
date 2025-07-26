package com.utez.objetosperdidos.controller;

import com.utez.objetosperdidos.model.ObjetoPerdido;
import com.utez.objetosperdidos.model.dao.ObjetoDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EditarObjetoController {

    @FXML private TextField tituloField;
    @FXML private TextField aulaField;
    @FXML private TextArea descripcionArea;
    @FXML private TextField fotoUrlField;

    private ObjetoPerdido objeto;
    private final ObjetoDAO dao = new ObjetoDAO();

    public void setObjeto(ObjetoPerdido objeto) {
        this.objeto = objeto;
        tituloField.setText(objeto.getNombreObjeto());
        aulaField.setText(objeto.getAula());
        descripcionArea.setText(objeto.getDescripcion());
        fotoUrlField.setText(objeto.getFotoUrl());
    }

    @FXML
    public void guardarCambios() {
        objeto.setNombreObjeto(tituloField.getText());
        objeto.setAula(aulaField.getText());
        objeto.setDescripcion(descripcionArea.getText());
        objeto.setFotoUrl(fotoUrlField.getText());


        boolean actualizado = dao.actualizarObjeto(objeto);
        Alert alert = new Alert(actualizado ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(actualizado ? "Objeto actualizado correctamente." : "Error al actualizar el objeto.");
        alert.showAndWait();

        if (actualizado) {
            ((Stage) tituloField.getScene().getWindow()).close();
        }
    }

    @FXML
    public void cancelar() {
        ((Stage) tituloField.getScene().getWindow()).close();
    }
}
