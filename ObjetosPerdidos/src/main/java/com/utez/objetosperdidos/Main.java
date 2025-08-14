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
            mainStage.setMaximized(true);
            mainStage.setResizable(false);
        } else {
            mainStage.setResizable(false);
        }

        mainStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}