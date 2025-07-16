package com.utez.objetosperdidos.controller;

import com.utez.objetosperdidos.Main;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class PrivacyController {
    private static String origen = "login"; 

    public static void setOrigen(String desdeDondeVengo) {
        origen = desdeDondeVengo;
    }

    @FXML private CheckBox acceptCheck;
    @FXML private Label messageLabel;

    @FXML
    public void onContinue() throws Exception {
        messageLabel.setText("");

        if (!acceptCheck.isSelected()) {
            messageLabel.setText("Debes aceptar las políticas para continuar.");
            return;
        }

       
        Main.switchScene("Login.fxml");
        origen = "login"; 
    }

    @FXML
    public void onCancel() throws Exception {
        Main.switchScene(origen.equals("registro") ? "Register.fxml" : "Login.fxml");
        origen = "login";
    }
}
