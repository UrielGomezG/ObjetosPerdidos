package com.utez.objetosperdidos.controller;

import com.utez.objetosperdidos.Main;
import com.utez.objetosperdidos.model.User;
import com.utez.objetosperdidos.util.Session;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox rememberCheck;
    @FXML private Label messageLabel;
    @FXML private ImageView imagenDecorativa;

    @FXML
    public void initialize() {
        try {
            Image imagen = new Image(getClass().getResource("/com/utez/objetosperdidos/imagenes/Vacio2.png").toExternalForm());
            imagenDecorativa.setImage(imagen);
        } catch (Exception e) {
            System.out.println("Error al cargar la imagen decorativa: " + e.getMessage());
        }
    }

    @FXML
    public void onSignIn() throws Exception {
        String email = emailField.getText().trim();
        String pwd = passwordField.getText();

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
            messageLabel.setStyle("-fx-text-fill: #388E3C;");

            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(event -> {
                try {
                    Main.switchScene("HomeView.fxml");
                } catch (Exception e) {
                    e.printStackTrace();
                    messageLabel.setText("Error al cargar la siguiente vista.");
                    messageLabel.setStyle("-fx-text-fill: #D32F2F;");
                }
            });
            pause.play();
        } else {
            messageLabel.setText("Credenciales inválidas. Intenta de nuevo.");
            messageLabel.setStyle("-fx-text-fill: #D32F2F;");
            passwordField.clear();
        }
    }

    @FXML
    public void onSignUpLink() throws Exception {
        Main.switchScene("RegisterView.fxml");
    }

    @FXML
    public void onPrivacy() throws Exception {
        Main.switchScene("Privacy.fxml");
    }
}
