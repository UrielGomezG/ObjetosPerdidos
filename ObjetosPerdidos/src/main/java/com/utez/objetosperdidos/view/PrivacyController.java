package com.utez.objetosperdidos.view;

import com.utez.objetosperdidos.Main;
import com.utez.objetosperdidos.model.User; // Aunque no se usa directamente aquí, la mantendremos por consistencia si User se usara en otra lógica de privacidad
import com.utez.objetosperdidos.util.Session; // Idem, si Session se usara en otra lógica de privacidad
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color; // Importar Color para el texto del mensaje

public class PrivacyController {
    @FXML CheckBox agreeCheck;
    @FXML Label messageLabel; // Añadimos un Label para mostrar mensajes al usuario

    // Método para inicializar el controlador si es necesario (opcional para esta vista)
    @FXML public void initialize() {
        // Podrías poner lógica de inicialización aquí si fuera necesario
        // Por ejemplo, cargar el texto del aviso de privacidad desde un archivo
    }

    @FXML public void onCancel() throws Exception {
        // Al cancelar, siempre regresamos a la vista de Login
        Main.switchScene("LoginView.fxml");
    }

    @FXML public void onContinue() throws Exception {
        if (!agreeCheck.isSelected()) {
            // Si el checkbox no está seleccionado, mostramos un mensaje de error
            messageLabel.setText("Debes aceptar el aviso de privacidad para continuar.");
            messageLabel.setTextFill(Color.RED); // Ponemos el texto en rojo para indicar un error
            return; // No avanzamos a la siguiente pantalla
        }

        // Si el checkbox está seleccionado, procedemos
        // Aquí podrías guardar la preferencia del usuario en la sesión o en la base de datos
        // Por ejemplo: Session.currentUser.setAcceptedPrivacy(true); (Si tu modelo User lo soporta)

        messageLabel.setText("Aviso aceptado. Redirigiendo...");
        messageLabel.setTextFill(Color.GREEN); // Texto verde para indicar éxito

        // Un pequeño retraso para que el usuario vea el mensaje de éxito antes de la redirección
        // Aunque para un "Aviso de Privacidad", un cambio instantáneo también es aceptable
        // PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
        // pause.setOnFinished(event -> {
        Main.switchScene("LoginView.fxml"); // O a otra vista si el flujo de la app lo requiere
        // });
        // pause.play();
    }
}