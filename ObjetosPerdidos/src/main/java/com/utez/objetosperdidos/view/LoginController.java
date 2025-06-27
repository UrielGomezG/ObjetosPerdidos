package com.utez.objetosperdidos.view;

import com.utez.objetosperdidos.Main;
import com.utez.objetosperdidos.model.User;
import com.utez.objetosperdidos.util.Session;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class LoginController {
    @FXML TextField emailField;
    @FXML PasswordField passwordField;
    @FXML CheckBox rememberCheck;
    @FXML Label messageLabel;

    @FXML public void initialize() {
        messageLabel.setText("");
        messageLabel.getStyleClass().add("message-label");
    }

    @FXML public void onSignIn() throws Exception {
        String email = emailField.getText().trim();
        String pwd = passwordField.getText();

        messageLabel.setText("");
        messageLabel.getStyleClass().remove("success");
        messageLabel.getStyleClass().remove("error");

        if (email.isEmpty() || pwd.isEmpty()) {
            messageLabel.setText("Por favor, ingresa tu correo y contraseña.");
            messageLabel.getStyleClass().add("error");
            return;
        }

        User found = Session.users.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(pwd))
                .findFirst().orElse(null);

        if (found != null) {
            Session.currentUser = found;
            messageLabel.setText("Inicio de sesión exitoso. Redirigiendo...");
            messageLabel.getStyleClass().add("success");
            
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(event -> {
                try {
                    Main.switchScene("HomeView.fxml");
                } catch (Exception e) {
                    e.printStackTrace();
                    messageLabel.setText("Error al cargar la siguiente vista.");
                    messageLabel.getStyleClass().add("error");
                }
            });
            pause.play();

        } else {
            messageLabel.setText("Credenciales inválidas. Intenta de nuevo.");
            messageLabel.getStyleClass().add("error");
            passwordField.clear();
        }
    }

    @FXML public void onSignUpLink() throws Exception {
        Main.switchScene("RegisterView.fxml");
    }

    @FXML public void onPrivacy() throws Exception {
        Main.switchScene("PrivacyView.fxml");
    }
}