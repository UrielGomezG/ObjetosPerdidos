package com.utez.objetosperdidos.view;

import com.utez.objetosperdidos.Main;
import com.utez.objetosperdidos.model.User;
import com.utez.objetosperdidos.util.Session;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color; // Importamos Color para los mensajes
import javafx.animation.PauseTransition; // Para un pequeño retraso visual
import javafx.util.Duration; // Para Duración en PauseTransition

public class RegisterController {
    @FXML TextField nameField, matriculaField, phoneField, emailField;
    @FXML PasswordField passwordField;
    // @FXML CheckBox rememberCheck; // Eliminado: no es común en registro
    @FXML Label messageLabel;

    @FXML public void onSignUp() throws Exception {
        // Limpiamos el mensaje anterior al iniciar un nuevo intento
        messageLabel.setText("");
        messageLabel.setTextFill(Color.RED); // Color por defecto para errores

        String nombre = nameField.getText().trim();
        String matricula = matriculaField.getText().trim();
        String telefono = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        // 1. Validaciones de campos vacíos
        if (nombre.isEmpty() || matricula.isEmpty() || telefono.isEmpty() || email.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Todos los campos son obligatorios.");
            return;
        }

        // 2. Validaciones de formato
        if (!nombre.matches("^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$")) { // Permite espacios y tildes
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

        if (password.length() < 8) { // Contraseña más robusta (mín. 8 caracteres)
            messageLabel.setText("La contraseña debe tener al menos 8 caracteres.");
            return;
        }

        // 3. Validación de existencia de usuario (Opcional, pero muy recomendado)
        // Antes de crear un nuevo usuario, verifica si el email o matrícula ya existen
        boolean emailExists = Session.users.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
        if (emailExists) {
            messageLabel.setText("Este correo electrónico ya está registrado.");
            return;
        }

        // Si la matrícula es un identificador único, también deberías validarla
        boolean matriculaExists = Session.users.stream().anyMatch(u -> u.getMatricula().equalsIgnoreCase(matricula));
        if (matriculaExists) {
            messageLabel.setText("Esta matrícula ya está registrada.");
            return;
        }


        // Si todas las validaciones pasan:
        User nuevo = new User(nombre, email, password, telefono, matricula);
        Session.users.add(nuevo);

        messageLabel.setText("Registro exitoso. Redirigiendo al Aviso de Privacidad...");
        messageLabel.setTextFill(Color.web("#2ECC71")); // Un verde vibrante para éxito

        // Pequeño retraso para que el usuario vea el mensaje de éxito
        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
        pause.setOnFinished(event -> {
            try {
                // Redirigir al Aviso de Privacidad
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