package com.utez.objetosperdidos.view;

import com.utez.objetosperdidos.Main;
import com.utez.objetosperdidos.model.User;
import com.utez.objetosperdidos.util.Session;
import com.utez.objetosperdidos.util.Session;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {
    @FXML TextField emailField;
    @FXML PasswordField passwordField;
    @FXML CheckBox rememberCheck;
    @FXML Label messageLabel;

    @FXML public void onSignIn() {
        String email = emailField.getText(), pwd = passwordField.getText();
        User found = Session.users.stream()
            .filter(u -> u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(pwd))
            .findFirst().orElse(null);

        if (found != null) {
            messageLabel.setText("Login correcto: " + found.getName());
        } else {
            messageLabel.setText("Credenciales inválidas.");
        }
    }

    @FXML public void onSignUpLink() throws Exception {
        Main.switchScene("RegisterView.fxml");
    }

    @FXML public void onPrivacy() throws Exception {
        Main.switchScene("PrivacyView.fxml");
    }
}
