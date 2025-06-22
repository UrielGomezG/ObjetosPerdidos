package com.utez.objetosperdidos.view;

import com.utez.objetosperdidos.Main;
import com.utez.objetosperdidos.model.User;
import com.utez.objetosperdidos.util.Session;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class LoginController {
    @FXML TextField emailField;
    @FXML PasswordField passwordField;
    @FXML CheckBox rememberCheck;
    @FXML Label messageLabel;

    @FXML public void onSignIn() throws Exception {
        String email = emailField.getText().trim(); // Trim spaces
        String pwd = passwordField.getText();

        if (email.isEmpty() || pwd.isEmpty()) {
            messageLabel.setText("Por favor, ingresa tu correo y contraseña.");
            return; // Exit if fields are empty
        }

        User found = Session.users.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(pwd))
                .findFirst().orElse(null);

        if (found != null) {
            Session.currentUser = found;
            messageLabel.setText("Inicio de sesión exitoso. Redirigiendo...");
            messageLabel.setStyle("-fx-text-fill: #388E3C;"); // Green color for success

            // Short delay before switching scene for better UX
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(event -> {
                try {
                    Main.switchScene("HomeView.fxml");
                } catch (Exception e) {
                    e.printStackTrace();
                    messageLabel.setText("Error al cargar la siguiente vista.");
                    messageLabel.setStyle("-fx-text-fill: #D32F2F;"); // Red for error
                }
            });
            pause.play();

        } else {
            messageLabel.setText("Credenciales inválidas. Intenta de nuevo.");
            messageLabel.setStyle("-fx-text-fill: #D32F2F;"); // Red color for error
            // Clear password field for security
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