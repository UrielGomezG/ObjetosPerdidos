package com.utez.objetosperdidos.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.net.URL;

public class PrivacyController {

    private Stage currentStage;

    public Scene getPrivacyScene(Stage stage) {
        this.currentStage = stage;

        BorderPane root = new BorderPane();
        root.getStyleClass().add("privacy-view-root");

        VBox contentBox = new VBox();
        contentBox.getStyleClass().add("privacy-content-box");
        contentBox.setPadding(new Insets(30));
        contentBox.setSpacing(15);
        contentBox.setAlignment(Pos.TOP_LEFT);

        Label titleLabel = new Label("Políticas de Privacidad");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #333; -fx-padding: 0 0 20px 0;");

        String privacyText = 
            "**1. Recopilación de Información:**\n" +
            "Recopilamos información personal (nombre, correo, teléfono) que usted nos proporciona voluntariamente al registrarse o usar nuestros servicios. También podemos recopilar datos de uso y técnicos para mejorar la experiencia.\n\n" +
            "**2. Uso de la Información:**\n" +
            "La información recopilada se utiliza para operar, mantener y proporcionarle las funciones de nuestra aplicación, para comunicarnos con usted, para personalizar su experiencia y para fines de investigación y análisis que nos ayudan a mejorar nuestros servicios.\n\n" +
            "**3. Compartir Información:**\n" +
            "No vendemos, comercializamos ni alquilamos su información personal a terceros. Podemos compartir información con socios de confianza que nos ayudan a operar nuestra aplicación, siempre que dichas partes acuerden mantener esta información confidencial.\n\n" +
            "**4. Seguridad de los Datos:**\n" +
            "Implementamos una variedad de medidas de seguridad para mantener la seguridad de su información personal cuando ingresa, envía o accede a su información personal. Sin embargo, ninguna transmisión por Internet o método de almacenamiento electrónico es 100% seguro.\n\n" +
            "**5. Sus Derechos:**\n" +
            "Usted tiene derecho a acceder, corregir o eliminar su información personal en cualquier momento. Por favor, contáctenos para ejercer estos derechos.\n\n" +
            "**6. Cambios a esta Política:**\n" +
            "Nos reservamos el derecho de actualizar o cambiar nuestra Política de Privacidad en cualquier momento. Cualquier cambio será efectivo inmediatamente después de su publicación en esta página.\n\n" +
            "**Contacto:**\n" +
            "Si tiene preguntas sobre esta Política de Privacidad, contáctenos en privacidad@objetosperdidos.utez.com.";

        Label contentLabel = new Label(privacyText);
        contentLabel.setWrapText(true);
        contentLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555; -fx-line-spacing: 5px;");

        ScrollPane scrollPane = new ScrollPane(contentLabel);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.getStyleClass().add("privacy-scroll-pane");

        Button backButton = new Button("Volver");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(event -> {
            LoginController loginController = new LoginController();
            Scene loginScene = loginController.getLoginScene(currentStage); 
            currentStage.setScene(loginScene);
            currentStage.setFullScreen(true);
        });

        HBox backButtonBox = new HBox(backButton);
        backButtonBox.setAlignment(Pos.CENTER_RIGHT);
        backButtonBox.setPadding(new Insets(20, 0, 0, 0));

        contentBox.getChildren().addAll(titleLabel, scrollPane, backButtonBox);
        root.setCenter(contentBox);

        Scene scene = new Scene(root, 1000, 700);

        URL cssUrl = getClass().getResource("/com/utez/objetosperdidos/css/login-styles.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
            System.out.println("CSS cargado para Privacy: " + cssUrl.toExternalForm());
        } else {
            System.err.println("ERROR: No se encontró el CSS para Privacy. Ruta esperada: /com/utez/objetosperdidos/css/login-styles.css");
        }

        return scene;
    }
}