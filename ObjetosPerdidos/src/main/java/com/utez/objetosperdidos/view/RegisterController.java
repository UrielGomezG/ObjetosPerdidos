package com.utez.objetosperdidos.view;

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
    @FXML TextField nameField, matriculaField, phoneField, emailField;
    @FXML PasswordField passwordField;
    @FXML Label messageLabel;

    @FXML public void onSignUp() throws Exception {
        messageLabel.setText("");
        messageLabel.setTextFill(Color.RED);

        String nombre = nameField.getText().trim();
        String matricula = matriculaField.getText().trim();
        String telefono = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String rol = "estuiante";

        if (nombre.isEmpty() || matricula.isEmpty() || telefono.isEmpty() || email.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Todos los campos son obligatorios.");
            return;
        }

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

        User nuevo = new User(nombre, email, password, telefono, matricula, rol);
        Session.users.add(nuevo);

        messageLabel.setText("Registro exitoso. Redirigiendo al Aviso de Privacidad...");
        messageLabel.setTextFill(Color.web("#62C070"));

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
