package com.utez.objetosperdidos.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PrivacyFakeController {
    private static String origen = "login";

    public static void setOrigen(String desdeDondeVengo) {
        origen = desdeDondeVengo;
    }

    @FXML
    private CheckBox acceptCheck;
    @FXML
    private Label messageLabel;

    @FXML
    private Button btnCancelar;

    @FXML
    public void onContinue() throws Exception {
         
    }

     @FXML
    private void onCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    
}
