package com.utez.objetosperdidos.controller;

import javafx.fxml.FXML;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Editar_Perfil_Controller {
    
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
