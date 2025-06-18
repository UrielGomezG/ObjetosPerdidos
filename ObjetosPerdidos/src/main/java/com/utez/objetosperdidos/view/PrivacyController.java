package com.utez.objetosperdidos.view;

import com.utez.objetosperdidos.Main;
import com.utez.objetosperdidos.model.User;
import com.utez.objetosperdidos.util.Session;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class PrivacyController {
    @FXML CheckBox agreeCheck;

    @FXML public void onCancel() throws Exception {
        Main.switchScene("LoginView.fxml");
    }

    @FXML public void onContinue() throws Exception {
        if (!agreeCheck.isSelected()) {
            return;
        }
        Main.switchScene("LoginView.fxml");
    }
}
