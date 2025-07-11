package com.utez.objetosperdidos.view;

import java.net.URL;

import com.utez.objetosperdidos.model.User;
import com.utez.objetosperdidos.util.Session;

import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginController {

    private TextField emailField;
    private PasswordField passwordField;
    private Label messageLabel;
    private AnchorPane anchorImage;
    private Stage primaryStage;

    public Scene getLoginScene(Stage stage) {
        this.primaryStage = stage;

        
        BorderPane root = new BorderPane();
        root.getStyleClass().add("border-pane");

    
        HBox centerHBox = new HBox();
        centerHBox.getStyleClass().add("center-hbox");
        centerHBox.setAlignment(Pos.CENTER);

        
        VBox formVBox = new VBox(10);
        formVBox.getStyleClass().add("form-vbox");
        formVBox.setAlignment(Pos.CENTER_LEFT);

        Label welcomeLabel = new Label("¡Bienvenido!");
        welcomeLabel.getStyleClass().add("title-label-welcome");

        Label loginTitleLabel = new Label("Iniciar Sesión Objetos Perdidos");
        loginTitleLabel.getStyleClass().add("title-label-login");

        VBox inputVBox = new VBox(8);
        inputVBox.getStyleClass().add("input-vbox");
        inputVBox.setAlignment(Pos.CENTER_LEFT);

        
        Label emailLabel = new Label("Usuario/Correo");
        emailLabel.getStyleClass().add("field-label");
        emailField = new TextField();
        emailField.setPromptText("example@utez.edu.mx");
        emailField.getStyleClass().add("text-field");

        Label passwordLabel = new Label("Contraseña");
        passwordLabel.getStyleClass().add("field-label");
        passwordField = new PasswordField();
        passwordField.setPromptText("********");
        passwordField.getStyleClass().add("password-field");

      
        messageLabel = new Label();
        messageLabel.setWrapText(true);
        messageLabel.getStyleClass().add("message-label");

        
        Button signInButton = new Button("Iniciar Sesión");
        signInButton.getStyleClass().add("sign-in-button");
        signInButton.setOnAction(event -> onSignIn());

        HBox signInButtonHBox = new HBox(signInButton);
        signInButtonHBox.setAlignment(Pos.CENTER);

        
        Separator separator = new Separator();

        
        HBox signUpLinkHBox = new HBox();
        signUpLinkHBox.setAlignment(Pos.CENTER_LEFT);
        signUpLinkHBox.getChildren().add(new Label("¿No tienes una cuenta?"));
        Hyperlink signUpLink = new Hyperlink("Registrarse");
        signUpLink.getStyleClass().add("hyperlink");
        signUpLink.setOnAction(event -> onSignUpLink());
        signUpLinkHBox.getChildren().add(signUpLink);

        HBox privacyLinkHBox = new HBox();
        privacyLinkHBox.setAlignment(Pos.CENTER);
        Hyperlink privacyLink = new Hyperlink("Leer Políticas de Privacidad");
        privacyLink.getStyleClass().add("hyperlink");
        privacyLink.setOnAction(event -> onPrivacy());
        privacyLinkHBox.getChildren().add(privacyLink);

        
        inputVBox.getChildren().addAll(
                emailLabel, emailField,
                passwordLabel, passwordField,
                messageLabel,
                signInButtonHBox,
                separator,
                signUpLinkHBox,
                privacyLinkHBox
        );

        formVBox.getChildren().addAll(
                welcomeLabel,
                loginTitleLabel,
                inputVBox
        );

        
        anchorImage = new AnchorPane();
        anchorImage.getStyleClass().add("image-anchor-pane");
        loadBackgroundImage();

        
        centerHBox.getChildren().addAll(formVBox, anchorImage);
        root.setCenter(centerHBox);

        
        Scene scene = new Scene(root, 1000, 700);

        
        URL cssUrl = getClass().getResource("/com/utez/objetosperdidos/css/login-styles.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
            System.out.println("[✔] CSS cargado correctamente.");
        } else {
            System.err.println("[✖] ERROR: No se encontró login-styles.css");
        }

        primaryStage.setFullScreen(true);
        return scene;
    }

    private void loadBackgroundImage() {
        try {
            URL url = getClass().getResource("/com/utez/objetosperdidos/images/Vacio2.png");
            if (url != null) {
                Image image = new Image(url.toExternalForm());
                ImageView imageView = new ImageView(image);

                imageView.setPreserveRatio(false);
                imageView.setSmooth(true);
                imageView.setCache(true);

                imageView.fitWidthProperty().bind(anchorImage.widthProperty());
                imageView.fitHeightProperty().bind(anchorImage.heightProperty());

                AnchorPane.setTopAnchor(imageView, 0.0);
                AnchorPane.setBottomAnchor(imageView, 0.0);
                AnchorPane.setLeftAnchor(imageView, 0.0);
                AnchorPane.setRightAnchor(imageView, 0.0);

                anchorImage.getChildren().add(imageView);

                System.out.println("[✔] Imagen Vacio2.png cargada correctamente.");
            } else {
                System.err.println("[✖] ERROR: No se encontró la imagen Vacio2.png");
            }
        } catch (Exception e) {
            System.err.println("[✖] Error cargando imagen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void onSignIn() {
        String email = emailField.getText().trim();
        String pwd = passwordField.getText();

        messageLabel.setText("");
        messageLabel.getStyleClass().remove("success");
        messageLabel.getStyleClass().remove("error");

        if (email.isEmpty() || pwd.isEmpty()) {
            messageLabel.setText("Por favor, ingresa tu correo y contraseña.");
            messageLabel.getStyleClass().add("error");
            return;
        }

        User found = Session.users.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(pwd))
                .findFirst()
                .orElse(null);

        if (found != null) {
            Session.currentUser = found;
            messageLabel.setText("Inicio de sesión exitoso.");
            messageLabel.getStyleClass().add("success");

            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(event -> {
                HomeController homeController = new HomeController();
                Scene homeScene = homeController.getHomeScene(primaryStage);
                primaryStage.setScene(homeScene);
                primaryStage.setFullScreen(true);
            });
            pause.play();

        } else {
            messageLabel.setText("Credenciales inválidas.");
            messageLabel.getStyleClass().add("error");
            passwordField.clear();
        }
    }

    private void onSignUpLink() {
        RegisterController registerController = new RegisterController();
        Scene registerScene = registerController.getRegisterScene(primaryStage);
        primaryStage.setScene(registerScene);
        primaryStage.setFullScreen(true);
    }

    private void onPrivacy() {
        PrivacyController privacyController = new PrivacyController();
        Scene privacyScene = privacyController.getPrivacyScene(primaryStage);
        primaryStage.setScene(privacyScene);
        primaryStage.setFullScreen(true);
    }
}
