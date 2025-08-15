package com.utez.objetosperdidos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage mainStage;

    @Override
    public void start(Stage stage) throws Exception {
        mainStage = stage;

        Parent root = FXMLLoader.load(getClass().getResource("/com/utez/objetosperdidos/view/Login.fxml"));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Objetos Perdidos");
        stage.show();
    }

    public static void switchScene(String fxmlName) throws Exception {
        Parent root = FXMLLoader.load(Main.class.getResource("/com/utez/objetosperdidos/view/" + fxmlName));
        Scene scene = new Scene(root);
        mainStage.setScene(scene);

        if (fxmlName.equals("HomeView.fxml") || fxmlName.equals("AdminHomeView.fxml")) {
            mainStage.setResizable(true);
            mainStage.show();
            mainStage.setMaximized(true);
            mainStage.setResizable(false);
        }
        else if (fxmlName.equals("Login.fxml")) {
            mainStage.setResizable(true);
            mainStage.setFullScreen(false);
            mainStage.setWidth(900);
            mainStage.setHeight(600);
            mainStage.centerOnScreen();
            mainStage.show();
            mainStage.setResizable(false);
        }
        else {
            mainStage.setResizable(true);
            mainStage.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}