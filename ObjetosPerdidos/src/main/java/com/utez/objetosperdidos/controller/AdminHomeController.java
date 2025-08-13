package com.utez.objetosperdidos.controller;

import java.io.IOException;

import com.utez.objetosperdidos.util.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.utez.objetosperdidos.model.User;

public class AdminHomeController {
    @FXML
    private BorderPane mainContainer;
    @FXML
    private Label userNameLabel;

    @FXML
    public void initialize() {
        mostrarVistaObjetos();

        if (Session.currentUser != null) {
            String nombreCompleto = Session.currentUser.getName() + " " + Session.currentUser.getApellidoPaterno();
            userNameLabel.setText(nombreCompleto);
        }
    }

    public void mostrarVistaObjetos() {
        try {
            FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/utez/objetosperdidos/view/objetos.admin.fxml"));
            Parent root = loader.load();
            mainContainer.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onPerfil(ActionEvent event) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/utez/objetosperdidos/view/Editar_Perfil.fxml"));
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

    @FXML
    private void onAgregarObjeto(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/utez/objetosperdidos/view/Registrar_Objeto.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Agregar nuevo objeto perdido");
            stage.setScene(new Scene(root));
            
            // Refresh de la vista
            stage.setOnHidden(e -> mostrarVistaObjetos());
            
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onLogout(ActionEvent event) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/utez/objetosperdidos/view/LogOut.fxml"));
            Parent root = loader.load();
            Stage modal = new Stage();
            modal.setTitle("¿Cerrar sesión?");
            modal.setScene(new Scene(root));
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al cargar modal Salir");
        }
    }
    @FXML
    private void abrirHistorialEntregados() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/utez/objetosperdidos/view/Historial.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Historial de Objetos Entregados");
        stage.setScene(new Scene(root));
        stage.setMaximized(true);
        stage.show();
    }

}
