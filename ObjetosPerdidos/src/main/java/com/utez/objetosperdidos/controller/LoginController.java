package com.utez.objetosperdidos.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.utez.objetosperdidos.Main;
import com.utez.objetosperdidos.model.User;
import com.utez.objetosperdidos.util.ConexionOracle;
import com.utez.objetosperdidos.util.Session;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
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

    try (Connection conn = ConexionOracle.getConnection()) {
        PreparedStatement stmt = conn.prepareStatement(
            "SELECT nombre, correo, contrasena, rol_id FROM USUARIOS WHERE correo = ? AND contrasena = ?"
        );
        stmt.setString(1, email);
        stmt.setString(2, pwd);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            int rol = rs.getInt("rol_id");
            User user = new User(
                rs.getString("nombre"),
                rs.getString("correo"),
                rs.getString("contrasena"),
                null,
                null, 
                rs.getInt("rol_id")
            );
            Session.currentUser = user;

            messageLabel.setText("Inicio de sesión exitoso. Mostrando aviso...");
            messageLabel.setStyle("-fx-text-fill: #388E3C;");

            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/utez/objetosperdidos/view/Privacy.fxml"));
                    Parent root = loader.load();
                    Stage modal = new Stage();
                    modal.setTitle("Aviso de Privacidad");
                    modal.setScene(new Scene(root));
                    modal.initModality(Modality.APPLICATION_MODAL);
                    modal.show();
                } catch (Exception ex) {
                    messageLabel.setText("Error al mostrar aviso.");
                    messageLabel.setStyle("-fx-text-fill: #D32F2F;");
                    ex.printStackTrace();
                }
            });
            pause.play();

        } else {
            messageLabel.setText("Credenciales inválidas. Intenta de nuevo.");
            messageLabel.setStyle("-fx-text-fill: #D32F2F;");
            passwordField.clear();
        }
    } catch (SQLException e) {
        e.printStackTrace();
        messageLabel.setText("Error de conexión.");
        messageLabel.setStyle("-fx-text-fill: #D32F2F;");
    }
}


    @FXML
    public void onSignUpLink() throws Exception {
        Main.switchScene("Register.fxml");
    }

    @FXML
    public void onPrivacy() throws Exception {
    
        PrivacyController.setOrigen("login");
        Main.switchScene("Privacy.fxml");
    }
}
