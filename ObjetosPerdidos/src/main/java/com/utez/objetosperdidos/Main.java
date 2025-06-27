package com.utez.objetosperdidos;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage primary;

    @Override
public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/utez/objetosperdidos/view/LoginView.fxml"));
    Scene scene = new Scene(fxmlLoader.load());
    stage.setTitle("Objetos Perdidos");
    stage.setScene(scene);
    stage.show();
}

    public static void switchScene(String fxml) throws Exception {
        primary.getScene().setRoot(
            FXMLLoader.load(Main.class.getResource("/com/utez/objetosperdidos/view/" + fxml))
        );
    }

    public static void main(String[] args) {
        launch(args);
    }
}