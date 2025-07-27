package com.utez.objetosperdidos.controller;

import com.utez.objetosperdidos.Main;
import com.utez.objetosperdidos.util.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class LogOutController{

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

     @FXML
    private Button btnAceptar;

    @FXML
    private void onContinue(){
        try {
            Main.switchScene("Login.fxml"); 
            closeWindow();
        } catch (Exception e) {
            e.printStackTrace(); 
        }
    }
}
