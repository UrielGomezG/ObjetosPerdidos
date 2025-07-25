package com.utez.objetosperdidos.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AdminHomeController {
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

public void onPerfil(ActionEvent event) throws Exception {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/utez/objetosperdidos/view/Editar_Perfil.fxml"));
                    Parent root = loader.load();
                    Stage modal = new Stage();
                    modal.setTitle("Editar Perfil");
                    modal.setScene(new Scene(root));
                    modal.initModality(Modality.APPLICATION_MODAL);
                    modal.show();
    } catch (Exception e) {
        System.out.println("Error al cargar modal editar perfil");
    }
}


}
