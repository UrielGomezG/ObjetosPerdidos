package com.utez.objetosperdidos.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomeController {

    public Scene getHomeScene(Stage stage) {
        VBox root = new VBox(20);
        root.setStyle("-fx-alignment: center; -fx-background-color: linear-gradient(to bottom right, #f0f8ff, #add8e6);");

        Label welcomeLabel = new Label("Bienvenido a Objetos Perdidos");
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #2c3e50;");

        Button logoutButton = new Button("Cerrar Sesión");
        logoutButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 16px;");

        logoutButton.setOnAction(event -> {
            LoginController loginController = new LoginController();
            Scene loginScene = loginController.getLoginScene(stage);
            stage.setScene(loginScene);
        });

        root.getChildren().addAll(welcomeLabel, logoutButton);

        return new Scene(root, 1000, 700);
    }
}
