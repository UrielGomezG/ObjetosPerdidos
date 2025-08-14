package com.utez.objetosperdidos.controller;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.utez.objetosperdidos.model.dao.UserAdminDAO;
import com.utez.objetosperdidos.util.Session;

public class Editar_Perfil_Admin_Controller {

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellidoPaterno;
    @FXML
    private TextField txtApellidoMaterno;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPassword;
    @FXML
    private Button btnCancelar;

    private final UserAdminDAO dao = new UserAdminDAO();

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

        // Si el campo está vacío, se mantiene el valor original
        String nombre = txtNombre.getText().isEmpty() ? Session.currentUser.getName() : txtNombre.getText();
        String apellidoPaterno = txtApellidoPaterno.getText().isEmpty() ? Session.currentUser.getApellidoPaterno()
                : txtApellidoPaterno.getText();
        String apellidoMaterno = txtApellidoMaterno.getText().isEmpty() ? Session.currentUser.getApellidoMaterno()
                : txtApellidoMaterno.getText();
        String email = txtEmail.getText().isEmpty() ? Session.currentUser.getEmail() : txtEmail.getText();
        String password = txtPassword.getText().isEmpty() ? Session.currentUser.getPassword() : txtPassword.getText();

        // Llamamos al método DAO para actualizar
        boolean actualizado = dao.updateUsuario(id, nombre, apellidoPaterno, apellidoMaterno, email, password);

        if (actualizado) {
            System.out.println("Usuario actualizado.");

            // Actualizamos la sesión también
            Session.currentUser.setName(nombre);
            Session.currentUser.setApellidoPaterno(apellidoPaterno);
            Session.currentUser.setApellidoMaterno(apellidoMaterno);
            Session.currentUser.setEmail(email);
            Session.currentUser.setPassword(password);

            mostrarInfo("Usuario actualizado exitosamente");
            onCancel();

        } else {
            System.out.println("Error al actualizar.");
        }
    }

    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void onCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
}