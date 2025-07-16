package com.utez.objetosperdidos.controller;

import com.utez.objetosperdidos.Main;
import com.utez.objetosperdidos.model.User;
import com.utez.objetosperdidos.util.Session;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class RegisterController {
    @FXML private TextField nameField, matriculaField, phoneField, emailField;
    @FXML private PasswordField passwordField, confirmPasswordField;
    @FXML private Label messageLabel;

    @FXML
    public void onSignUp() {
        messageLabel.setText("");
        messageLabel.setTextFill(Color.RED);

        String nombre = nameField.getText().trim();
        String matricula = matriculaField.getText().trim();
        String telefono = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String rol = "estudiante";

        if (nombre.isEmpty() || matricula.isEmpty() || telefono.isEmpty() ||
                email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            messageLabel.setText("Todos los campos son obligatorios.");
            return;
        }

        if (!nombre.matches("^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$")) {
            messageLabel.setText("El nombre solo debe contener letras.");
            return;
        }

        if (!matricula.matches("^[a-zA-Z0-9]+$")) {
            messageLabel.setText("La matrícula debe ser alfanumérica.");
            return;
        }

        if (!telefono.matches("^\\d{10}$")) {
            messageLabel.setText("El teléfono debe tener 10 dígitos.");
            return;
        }

        if (!email.matches("^[\\w.-]+@utez\\.edu\\.mx$")) {
            messageLabel.setText("Usa un correo institucional válido.");
            return;
        }

        if (password.length() < 8) {
            messageLabel.setText("La contraseña debe tener al menos 8 caracteres.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Las contraseñas no coinciden.");
            return;
        }

        boolean emailExists = Session.users.stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
        if (emailExists) {
            messageLabel.setText("Este correo ya está registrado.");
            return;
        }

        boolean matriculaExists = Session.users.stream()
                .anyMatch(u -> u.getMatricula().equalsIgnoreCase(matricula));
        if (matriculaExists) {
            messageLabel.setText("Esta matrícula ya está registrada.");
            return;
        }

    
        User nuevo = new User(nombre, email, password, telefono, matricula, rol);
        Session.users.add(nuevo);

        messageLabel.setText("Registro exitoso. Redirigiendo...");
        messageLabel.setTextFill(Color.web("#62C070"));

        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
        pause.setOnFinished(e -> {
            try {
            
                PrivacyController.setOrigen("registro");
                Main.switchScene("Privacy.fxml");
            } catch (Exception ex) {
                messageLabel.setText("Error al cargar la vista.");
                messageLabel.setTextFill(Color.RED);
            }
        });
        pause.play();
    }

    @FXML
    public void onSignInLink() throws Exception {
        Main.switchScene("Login.fxml");
    }

    @FXML
    public void onPrivacyLink() throws Exception {
        PrivacyController.setOrigen("registro");
        Main.switchScene("Privacy.fxml");
    }
}
