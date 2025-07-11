package com.utez.objetosperdidos.view;

import java.net.URL;

import com.utez.objetosperdidos.model.User;
import com.utez.objetosperdidos.util.Session;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class RegisterController {

    private Stage currentStage;
    private TextField nameField;
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private TextField phoneField;
    private TextField matriculaField;
    private ComboBox<String> roleComboBox;
    private Label messageLabel;

    public Scene getRegisterScene(Stage stage) {
        this.currentStage = stage;

        BorderPane root = new BorderPane();
        root.getStyleClass().add("register-view-root");

        VBox contentBox = new VBox(20);
        contentBox.getStyleClass().add("register-content-box");
        contentBox.setPadding(new Insets(40));
        contentBox.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Crear Nueva Cuenta");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #333;");

        GridPane formGrid = new GridPane();
        formGrid.setVgap(15);
        formGrid.setHgap(10);
        formGrid.setAlignment(Pos.CENTER_LEFT);

        // Campos de texto y labels
        Label nameLabel = new Label("Nombre:");
        nameField = new TextField();
        nameField.setPromptText("Tu nombre");
        nameField.getStyleClass().add("text-field");

        Label emailLabel = new Label("Correo Electrónico:");
        emailField = new TextField();
        emailField.setPromptText("ejemplo@utez.edu.mx");
        emailField.getStyleClass().add("text-field");

        Label passwordLabel = new Label("Contraseña:");
        passwordField = new PasswordField();
        passwordField.setPromptText("Mínimo 6 caracteres");
        passwordField.getStyleClass().add("password-field");

        Label confirmPasswordLabel = new Label("Confirmar Contraseña:");
        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirma tu contraseña");
        confirmPasswordField.getStyleClass().add("password-field");

        Label phoneLabel = new Label("Teléfono:");
        phoneField = new TextField();
        phoneField.setPromptText("Número telefónico");
        phoneField.getStyleClass().add("text-field");

        Label matriculaLabel = new Label("Matrícula:");
        matriculaField = new TextField();
        matriculaField.setPromptText("Tu matrícula");
        matriculaField.getStyleClass().add("text-field");

        Label roleLabel = new Label("Rol:");
        roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Usuario", "Admin");
        roleComboBox.setValue("Usuario"); // Valor por defecto

        formGrid.addRow(0, nameLabel, nameField);
        formGrid.addRow(1, emailLabel, emailField);
        formGrid.addRow(2, passwordLabel, passwordField);
        formGrid.addRow(3, confirmPasswordLabel, confirmPasswordField);
        formGrid.addRow(4, phoneLabel, phoneField);
        formGrid.addRow(5, matriculaLabel, matriculaField);
        formGrid.addRow(6, roleLabel, roleComboBox);

        messageLabel = new Label();
        messageLabel.getStyleClass().add("message-label");

        Button registerButton = new Button("Registrarse");
        registerButton.getStyleClass().add("sign-in-button");
        registerButton.setMaxWidth(Double.MAX_VALUE);
        registerButton.setOnAction(event -> onRegister());

        HBox buttonBox = new HBox(registerButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        Hyperlink backToLoginLink = new Hyperlink("Ya tengo una cuenta");
        backToLoginLink.getStyleClass().add("hyperlink");
        backToLoginLink.setOnAction(event -> {
            LoginController loginController = new LoginController();
            Scene loginScene = loginController.getLoginScene(currentStage);
            currentStage.setScene(loginScene);
            currentStage.setFullScreen(true);
        });

        HBox backLinkBox = new HBox(backToLoginLink);
        backLinkBox.setAlignment(Pos.CENTER);

        contentBox.getChildren().addAll(
                titleLabel,
                formGrid,
                messageLabel,
                buttonBox,
                backLinkBox
        );

        root.setCenter(contentBox);

        Scene scene = new Scene(root, 1000, 700);

        URL cssUrl = getClass().getResource("/com/utez/objetosperdidos/css/Register.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
            System.out.println("[✔] CSS cargado para Register.");
        } else {
            System.err.println("[✖] ERROR: No se encontró el CSS para Register.");
        }

        return scene;
    }

    private void onRegister() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String phone = phoneField.getText().trim();
        String matricula = matriculaField.getText().trim();
        String rol = roleComboBox.getValue();

        messageLabel.setText("");
        messageLabel.getStyleClass().removeAll("error", "success");

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() 
            || phone.isEmpty() || matricula.isEmpty()) {
            messageLabel.setText("Por favor, completa todos los campos.");
            messageLabel.getStyleClass().add("error");
            return;
        }

        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Las contraseñas no coinciden.");
            messageLabel.getStyleClass().add("error");
            return;
        }

        if (password.length() < 6) {
            messageLabel.setText("La contraseña debe tener al menos 6 caracteres.");
            messageLabel.getStyleClass().add("error");
            return;
        }

        boolean userExists = Session.users.stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));

        if (userExists) {
            messageLabel.setText("Ya existe una cuenta con ese correo.");
            messageLabel.getStyleClass().add("error");
            return;
        }

        // Crear el usuario con todos los datos
        User newUser = new User(name, email, password, phone, matricula, rol);
        Session.users.add(newUser);

        messageLabel.setText("Registro exitoso. Redirigiendo...");
        messageLabel.getStyleClass().add("success");

        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
        pause.setOnFinished(event -> {
            LoginController loginController = new LoginController();
            Scene loginScene = loginController.getLoginScene(currentStage);
            currentStage.setScene(loginScene);
            currentStage.setFullScreen(true);
        });
        pause.play();
    }
}
