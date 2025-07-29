package com.utez.objetosperdidos.controller;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.utez.objetosperdidos.model.dao.UserDAO;
import com.utez.objetosperdidos.util.Session;


public class Editar_Perfil_Controller {

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidoPaterno;
    @FXML private TextField txtApellidoMaterno;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPassword;

    private final UserDAO dao = new UserDAO();

    @FXML
    private void initialize() {
        if (Session.currentUser != null) {
            txtNombre.setText(Session.currentUser.getName());
            txtApellidoPaterno.setText(Session.currentUser.getApellidoPaterno());
            txtApellidoMaterno.setText(Session.currentUser.getApellidoMaterno());
            txtEmail.setText(Session.currentUser.getEmail());
            txtPassword.setText(Session.currentUser.getPassword());
        }
    }

    @FXML
    private void onActualizarUsuario(ActionEvent event) {
        int id = Session.currentUser.getId();
         String nombre = txtNombre.getText().isEmpty() ? Session.currentUser.getName() : txtNombre.getText();
        String apellidoPaterno = txtApellidoPaterno.getText().isEmpty() ? Session.currentUser.getApellidoPaterno() : txtApellidoPaterno.getText();
        String apellidoMaterno = txtApellidoMaterno.getText().isEmpty() ? Session.currentUser.getApellidoMaterno() : txtApellidoMaterno.getText();
        String email = txtEmail.getText().isEmpty() ? Session.currentUser.getEmail() : txtEmail.getText();
        String password = txtPassword.getText().isEmpty() ? Session.currentUser.getPassword() : txtPassword.getText();
        boolean actualizado = dao.updateUsuario(id, nombre, apellidoPaterno, apellidoMaterno, email, password);

        if (actualizado) {
            System.out.println("✅ Usuario actualizado.");
        } else {
            System.out.println("❌ Error al actualizar.");
        }
    }

    @FXML
    private Button btnCancelar;

    private void closeWindow(){
        Stage stage=(Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void onCancel(){
        closeWindow();
    }
}
