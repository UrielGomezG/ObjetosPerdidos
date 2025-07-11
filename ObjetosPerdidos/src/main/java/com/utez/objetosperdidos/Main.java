package com.utez.objetosperdidos;

import com.utez.objetosperdidos.view.LoginController;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            LoginController loginController = new LoginController();
            Scene loginScene = loginController.getLoginScene(primaryStage);

            primaryStage.setTitle("Objetos Perdidos UTEZ");
            primaryStage.setScene(loginScene);
            primaryStage.setFullScreen(true);
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Error al iniciar la aplicación: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
