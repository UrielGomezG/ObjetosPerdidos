package com.utez.objetosperdidos.controller;

import com.utez.objetosperdidos.Main;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

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
        if (origen.equals("registro")) {
            Main.switchScene("Register.fxml");
        } else {
            Main.switchScene("Login.fxml");
        }
        Stage currentStage = (Stage) acceptCheck.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    public void onCancel() throws Exception {
        Stage currentStage = (Stage) acceptCheck.getScene().getWindow();
        currentStage.close();
        if (origen.equals("registro")) {
            Main.switchScene("Register.fxml");
        } else {
            Main.switchScene("Login.fxml");
        }
        origen = "";
    }
}
