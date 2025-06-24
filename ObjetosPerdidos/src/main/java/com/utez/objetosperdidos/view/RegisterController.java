package com.utez.objetosperdidos.view;

import com.utez.objetosperdidos.Main;
import com.utez.objetosperdidos.model.User;
import com.utez.objetosperdidos.util.Session;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class RegisterController {
    @FXML TextField nameField, matriculaField, phoneField, emailField;
    @FXML PasswordField passwordField;
    @FXML Label messageLabel;

    @FXML public void onSignUp() throws Exception {
        // Reinicia el mensaje al inicio de cada intento de registro
        messageLabel.setText("");
        messageLabel.setTextFill(Color.RED); // Establece color predeterminado para errores

        String nombre = nameField.getText().trim();
        String matricula = matriculaField.getText().trim();
        String telefono = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        // 1. Validaciones de campos obligatorios
        if (nombre.isEmpty() || matricula.isEmpty() || telefono.isEmpty() || email.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Todos los campos son obligatorios.");
            return;
        }

        // 2. Validaciones de formato de los campos
        if (!nombre.matches("^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$")) {
            messageLabel.setText("El nombre solo debe contener letras y espacios.");
            return;
        }

        if (!matricula.matches("^[a-zA-Z0-9]+$")) {
            messageLabel.setText("La matrícula solo debe contener letras y números.");
            return;
        }

        if (!telefono.matches("^\\d{10}$")) {
            messageLabel.setText("El teléfono debe tener exactamente 10 dígitos numéricos.");
            return;
        }

        if (!email.matches("^[\\w.-]+@utez\\.edu\\.mx$")) {
            messageLabel.setText("El correo debe ser institucional (@utez.edu.mx).");
            return;
        }

        if (password.length() < 8) {
            messageLabel.setText("La contraseña debe tener al menos 8 caracteres.");
            return;
        }

        // 3. Validación de existencia de usuario para evitar duplicados
        boolean emailExists = Session.users.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
        if (emailExists) {
            messageLabel.setText("Este correo electrónico ya está registrado.");
            return;
        }

        boolean matriculaExists = Session.users.stream().anyMatch(u -> u.getMatricula().equalsIgnoreCase(matricula));
        if (matriculaExists) {
            messageLabel.setText("Esta matrícula ya está registrada.");
            return;
        }

        // Si todas las validaciones pasan, procede con el registro
        User nuevo = new User(nombre, email, password, telefono, matricula);
        Session.users.add(nuevo);

        // Muestra mensaje de éxito y redirige
        messageLabel.setText("Registro exitoso. Redirigiendo al Aviso de Privacidad...");
        messageLabel.setTextFill(Color.web("#62C070")); // Color verde para mensaje de éxito

        // Pequeño retraso para mejorar la experiencia del usuario antes de la redirección
        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
        pause.setOnFinished(event -> {
            try {
                Main.switchScene("PrivacyView.fxml");
            } catch (Exception e) {
                e.printStackTrace();
                messageLabel.setText("Error al cargar la siguiente vista.");
                messageLabel.setTextFill(Color.RED);
            }
        });
        pause.play();
    }

    @FXML public void onSignInLink() throws Exception {
        Main.switchScene("LoginView.fxml");
    }
}