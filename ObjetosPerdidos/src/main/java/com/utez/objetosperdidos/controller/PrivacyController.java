package com.utez.objetosperdidos.controller;

import com.utez.objetosperdidos.Main;
import com.utez.objetosperdidos.util.Session;
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

        Stage currentStage = (Stage) acceptCheck.getScene().getWindow();
        currentStage.close();

        if (Session.currentUser != null) {
            if (Session.currentUser.getRole() == 1) {
                Main.switchScene("AdminHomeView.fxml");
            } else {
                Main.switchScene("HomeView.fxml");
            }
        } else {
            Main.switchScene(origen.equals("registro") ? "Register.fxml" : "Login.fxml");
        }
    }

    @FXML
    public void onCancel() throws Exception {
        Stage currentStage = (Stage) acceptCheck.getScene().getWindow();
        currentStage.close();
        Main.switchScene(origen.equals("registro") ? "Register.fxml" : "Login.fxml");
        origen = "";
    }
}
