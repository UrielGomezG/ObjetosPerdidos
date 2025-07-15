
package com.utez.objetosperdidos.controller;

import com.utez.objetosperdidos.Main;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label; 
import javafx.scene.paint.Color;

public class PrivacyController {
    @FXML CheckBox agreeCheck;
    @FXML Label messageLabel; 

    
    @FXML public void initialize() {
       
    }

    @FXML public void onCancel() throws Exception {
        Main.switchScene("Login.fxml");
    }

    @FXML public void onContinue() throws Exception {
        if (!agreeCheck.isSelected()) {
            
            messageLabel.setText("Debes aceptar el aviso de privacidad para continuar.");
            messageLabel.setTextFill(Color.RED); 
            return; 
        }
        messageLabel.setText("Aviso aceptado. Redirigiendo...");
        messageLabel.setTextFill(Color.GREEN);

        Main.switchScene("Login.fxml");
        
    }
}
