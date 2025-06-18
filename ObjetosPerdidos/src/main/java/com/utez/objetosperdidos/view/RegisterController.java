package com.utez.objetosperdidos.view;

import com.utez.objetosperdidos.Main;
import com.utez.objetosperdidos.model.User;
import com.utez.objetosperdidos.util.Session;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RegisterController {
    @FXML TextField nameField, matriculaField, phoneField, emailField;
    @FXML PasswordField passwordField;
    @FXML CheckBox rememberCheck;
    @FXML Label messageLabel;

    @FXML public void onSignUp() throws Exception {

        if (nameField.getText().isEmpty() || matriculaField.getText().isEmpty()
            || emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            messageLabel.setText("Completa todos los campos.");
            return;
        }

        Main.switchScene("PrivacyView.fxml");
    }

    @FXML public void onSignInLink() throws Exception {
        Main.switchScene("LoginView.fxml");
    }
}
