package com.utez.objetosperdidos.controller;

import com.utez.objetosperdidos.model.ObjetoPerdido;
import com.utez.objetosperdidos.model.dao.ObjetoDAO;
import com.utez.objetosperdidos.util.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.text.FontWeight;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class HistorialObjetoController {

    @FXML
    private FlowPane itemsContainer;
    @FXML
    private Label userNameLabel;

    private final ObjetoDAO dao = new ObjetoDAO();

    @FXML
    public void initialize() {
        itemsContainer.prefWrapLengthProperty().bind(itemsContainer.widthProperty());

        List<ObjetoPerdido> objetos = dao.obtenerObjetosEntregados()
                .stream()
                .filter(o -> "Entregado".equalsIgnoreCase(o.getEstado()))
                .collect(Collectors.toList());

        if (Session.currentUser != null) {
            String nombreCompleto = Session.currentUser.getName() + " " + Session.currentUser.getApellidoPaterno();
            userNameLabel.setText(nombreCompleto);
        }

        mostrarObjetos(objetos);
    }

    private void mostrarObjetos(List<ObjetoPerdido> lista) {
        itemsContainer.getChildren().clear();
        for (ObjetoPerdido o : lista) {
            Image imagen = null;
            if (o.getFotoUrl() != null && !o.getFotoUrl().trim().isEmpty()) {
                Path rutaImagen = Paths.get(System.getProperty("user.home"), "Downloads", o.getFotoUrl().trim());
                if (Files.exists(rutaImagen)) {
                    imagen = new Image(rutaImagen.toUri().toString());
                }
            }

            VBox card = crearTarjeta(o.getNombreObjeto(), o.getDescripcion(), imagen, "Entregado".equalsIgnoreCase(o.getEstado()));
            itemsContainer.getChildren().add(card);
        }
    }

    private VBox crearTarjeta(String titulo, String descripcion, Image imagen, boolean entregado) {
        VBox tarjeta = new VBox(12);
        tarjeta.getStyleClass().add("card");
        tarjeta.setAlignment(Pos.TOP_LEFT);
        tarjeta.setPadding(new Insets(16));

        if (imagen != null) {
            ImageView imageView = new ImageView(imagen);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            imageView.setPreserveRatio(true);
            tarjeta.getChildren().add(imageView);
        }

        Label tituloLabel = new Label(titulo);
        tituloLabel.getStyleClass().add("card-title");

        Label descripcionLabel = new Label(descripcion);
        descripcionLabel.getStyleClass().add("card-description");
        descripcionLabel.setWrapText(true);

        tarjeta.getChildren().addAll(tituloLabel, descripcionLabel);

        if (entregado) {
            Label entregadoLabel = new Label("Entregado");
            entregadoLabel.getStyleClass().add("entregado-label");
            tarjeta.getChildren().add(entregadoLabel);
        }

        return tarjeta;
    }



    @FXML
    private void onHome(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void OnAgregarObjeto(ActionEvent event) {
        try {
            Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stageActual.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/utez/objetosperdidos/view/Registrar_Objeto.fxml"));
            Parent root = loader.load();

            Stage stageNuevo = new Stage();
            stageNuevo.setScene(new Scene(root));
            stageNuevo.setTitle("Agregar Objeto");
            stageNuevo.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}