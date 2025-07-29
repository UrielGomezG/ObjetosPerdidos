package com.utez.objetosperdidos.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import com.utez.objetosperdidos.model.dao.UserDAO;
import com.utez.objetosperdidos.util.Session;

public class UserController {
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidoPaterno;
    @FXML private TextField txtApellidoMaterno;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPassword;

    private final UserDAO dao = new UserDAO();

    @FXML
    private void onActualizarUsuario(ActionEvent event) {
        int id = Session.currentUser.getId();
        String nombre = txtNombre.getText();
        String apellidoPaterno = txtApellidoPaterno.getText();
        String apellidoMaterno = txtApellidoMaterno.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();

        boolean actualizado = dao.updateUsuario(id, nombre, apellidoPaterno, apellidoMaterno, email, password);

        if (actualizado) {
            System.out.println("✅ Usuario actualizado.");
        } else {
            System.out.println("❌ Error al actualizar.");
        }
    }

}
