package com.utez.objetosperdidos.view;

import com.utez.objetosperdidos.Main;
import com.utez.objetosperdidos.model.User;
import com.utez.objetosperdidos.util.Session;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.scene.paint.Color; // Importa Color

public class LoginController {
    @FXML TextField emailField;
    @FXML PasswordField passwordField;
    @FXML CheckBox rememberCheck;
    @FXML Label messageLabel;

    @FXML public void onSignIn() throws Exception {
        String email = emailField.getText().trim();
        String pwd = passwordField.getText();

        messageLabel.setText(""); // Limpiar mensaje anterior
        messageLabel.setTextFill(Color.RED); // Establecer color por defecto para errores

        if (email.isEmpty() || pwd.isEmpty()) {
            messageLabel.setText("Por favor, ingresa tu correo y contraseña.");
            return;
        }

        User found = Session.users.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(pwd))
                .findFirst().orElse(null);

        if (found != null) {
            Session.currentUser = found;
            messageLabel.setText("Inicio de sesión exitoso. Redirigiendo...");
            messageLabel.setTextFill(Color.web("#00796B")); // ¡VERDE OSCURO/TEAL para éxito!


            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(event -> {
                try {
                    Main.switchScene("HomeView.fxml");
                } catch (Exception e) {
                    e.printStackTrace();
                    messageLabel.setText("Error al cargar la siguiente vista.");
                    messageLabel.setTextFill(Color.web("#D32F2F")); // Rojo para error
                }
            });
            pause.play();

        } else {
            messageLabel.setText("Credenciales inválidas. Intenta de nuevo.");
            messageLabel.setTextFill(Color.web("#D32F2F")); // Rojo para error
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