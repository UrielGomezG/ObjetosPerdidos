package com.utez.objetosperdidos.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

public class HomeController {
@FXML
private BorderPane mainContainer;

@FXML
public void initialize() {
    mostrarVistaObjetos();
}

public void mostrarVistaObjetos() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/utez/objetosperdidos/view/objetos.fxml"));
        Parent vista = loader.load();
        mainContainer.setCenter(vista);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

}