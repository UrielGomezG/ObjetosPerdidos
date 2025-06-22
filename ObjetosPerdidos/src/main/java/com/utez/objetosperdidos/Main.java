package com.utez.objetosperdidos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage primary;

    @Override
    public void start(Stage stage) throws Exception {
        primary = stage;
        stage.setTitle("Objetos Perdidos");
        stage.setScene(new Scene(FXMLLoader.load(new java.io.File("src/main/java/com/utez/objetosperdidos/view/LoginView.fxml").toURI().toURL())));
        stage.show();
    }

    public static void switchScene(String fxml) throws Exception {
        primary.getScene().setRoot(
                FXMLLoader.load(new java.io.File("src/main/java/com/utez/objetosperdidos/view/" + fxml).toURI().toURL())

        );
    }

    public static void main(String[] args) {
        launch(args);
    }
}
