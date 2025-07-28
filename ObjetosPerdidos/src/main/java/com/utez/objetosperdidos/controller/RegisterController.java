package com.utez.objetosperdidos.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.utez.objetosperdidos.Main;
import com.utez.objetosperdidos.util.ConexionOracle;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class RegisterController {
    @FXML private TextField nameField, apellidoPaternoField, apellidoMaternoField ,matriculaField, phoneField, emailField;
    @FXML private PasswordField passwordField, confirmPasswordField;
    @FXML private Label messageLabel;

    @FXML
    public void onSignUp() {
        messageLabel.setText("");
        messageLabel.setTextFill(Color.RED);

        String nombre = nameField.getText().trim();
        String apellidoPaterno = apellidoPaternoField.getText().trim();
        String apellidoMaterno = apellidoMaternoField.getText().trim();
        String matricula = matriculaField.getText().trim();
        String telefono = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (nombre.isEmpty() || apellidoPaterno.isEmpty() || apellidoMaterno.isEmpty()||matricula.isEmpty() || telefono.isEmpty() ||
                email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            messageLabel.setText("Todos los campos son obligatorios.");
            return;
        }

        if (!nombre.matches("^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$")) {
            messageLabel.setText("El nombre solo debe contener letras.");
            return;
        }
        if (!apellidoPaterno.matches("^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$")) {
            messageLabel.setText("El nombre solo debe contener letras.");
            return;
        }
        if (!apellidoMaterno.matches("^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$")) {
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

        try (Connection conn = ConexionOracle.getConnection()) {        
            PreparedStatement checkEmail = conn.prepareStatement("SELECT COUNT(*) FROM USUARIOS WHERE correo = ?");
            checkEmail.setString(1, email);
            ResultSet rsEmail = checkEmail.executeQuery();
            if (rsEmail.next() && rsEmail.getInt(1) > 0) {
                messageLabel.setText("Este correo ya está registrado.");
                return;
            }

            PreparedStatement checkMatricula = conn.prepareStatement("SELECT COUNT(*) FROM ALUMNOS WHERE matricula = ?");
            checkMatricula.setString(1, matricula);
            ResultSet rsMatricula = checkMatricula.executeQuery();
            if (rsMatricula.next() && rsMatricula.getInt(1) > 0) {
                messageLabel.setText("Esta matrícula ya está registrada.");
                return;
            }
 // Insertar usuario y obtener ID
        String[] returnColumns = { "ID" }; // ← PARA OBTENER LA PK
        PreparedStatement insertUser = conn.prepareStatement(
            "INSERT INTO USUARIOS (nombre, apellidoPaterno, apellidoMaterno, correo, contrasena, rol_id) VALUES (?, ?, ?, ?, ?, 2)",
            returnColumns
        );  
        insertUser.setString(1, nombre);
        insertUser.setString(2, apellidoPaterno);
        insertUser.setString(3, apellidoMaterno);
        insertUser.setString(4, email);
        insertUser.setString(5, password);
        
        insertUser.executeUpdate();

        ResultSet generatedKeys = insertUser.getGeneratedKeys();
        if (generatedKeys.next()) {
            int userId = generatedKeys.getInt(1); 

            PreparedStatement insertAlumno = conn.prepareStatement(
                "INSERT INTO ALUMNOS (matricula, telefono, usuario_id) VALUES (?, ?, ?)"
            );
            insertAlumno.setString(1, matricula);
            insertAlumno.setString(2, telefono);
            insertAlumno.setInt(3, userId);
            insertAlumno.executeUpdate();

            messageLabel.setText("Registro exitoso. Redirigiendo...");
            messageLabel.setTextFill(Color.web("#62C070"));

            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(e -> {
                try {
                Main.switchScene("Login.fxml");
            } catch (Exception ex) {
            messageLabel.setText("Error al cargar la vista de inicio de sesión.");
            messageLabel.setTextFill(Color.RED);
            }
            });
            pause.play();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            messageLabel.setText("Error al registrar usuario.");
        }
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
