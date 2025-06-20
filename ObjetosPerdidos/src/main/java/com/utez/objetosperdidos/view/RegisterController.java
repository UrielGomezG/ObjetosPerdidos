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

        String nombre = nameField.getText();
        String matricula = matriculaField.getText();
        String telefono = phoneField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (nombre.isEmpty() || matricula.isEmpty() || telefono.isEmpty() || email.isEmpty() || password.isEmpty()) {
        messageLabel.setText("Completa todos los campos.");
        return;
    }

    if (!nombre.matches("^[A-Za-z]+$")) {
        messageLabel.setText("El nombre solo debe contener letras.");
        return;
    }

    if (!matricula.matches("^[a-zA-Z0-9]+$")) {
        messageLabel.setText("La matrícula solo debe contener letras y números.");
        return;
    }

    if (!telefono.matches("^\\d{10}$")) {
        messageLabel.setText("El teléfono debe tener exactamente 10 dígitos.");
        return;
    }

    if (!email.matches("^[\\w.-]+@utez\\.edu\\.mx$")) {
        messageLabel.setText("El correo debe ser institucional (@utez.edu.mx).");
        return;
    }

    if (password.length() < 5) {
        messageLabel.setText("La contraseña debe tener al menos 5 caracteres.");
        return;
    }

        User nuevo = new User(nombre, email, password, telefono, matricula);
        Session.users.add(nuevo);
        messageLabel.setText("Registro válido.");
        Main.switchScene("PrivacyView.fxml");
    }

    @FXML public void onSignInLink() throws Exception {
        Main.switchScene("LoginView.fxml");
    }
}
