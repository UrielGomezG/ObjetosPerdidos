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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

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
            VBox card = crearTarjeta(o);
            itemsContainer.getChildren().add(card);
        }
    }

    private VBox crearTarjeta(ObjetoPerdido objeto) {
        VBox tarjeta = new VBox(12);
        tarjeta.getStyleClass().add("card");
        tarjeta.setAlignment(Pos.TOP_LEFT);
        tarjeta.setPadding(new Insets(16));

        // Imagen del objeto
        if (objeto.getFotoUrl() != null && !objeto.getFotoUrl().trim().isEmpty()) {
            Path rutaImagen = Paths.get(System.getProperty("user.home"), "Downloads", objeto.getFotoUrl().trim());
            if (Files.exists(rutaImagen)) {
                ImageView imageView = new ImageView(new Image(rutaImagen.toUri().toString()));
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
                imageView.setPreserveRatio(true);
                tarjeta.getChildren().add(imageView);
            }
        }

        Label tituloLabel = new Label(objeto.getNombreObjeto());
        tituloLabel.getStyleClass().add("card-title");

        Label descripcionLabel = new Label(objeto.getDescripcion());
        descripcionLabel.getStyleClass().add("card-description");
        descripcionLabel.setWrapText(true);

        tarjeta.getChildren().addAll(tituloLabel, descripcionLabel);

        if ("Entregado".equalsIgnoreCase(objeto.getEstado())) {
            Label entregadoLabel = new Label("Entregado");
            entregadoLabel.getStyleClass().add("entregado-label");
            tarjeta.getChildren().add(entregadoLabel);
        }


        tarjeta.setOnMouseClicked(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/utez/objetosperdidos/view/VerObjetoAdmin.fxml"));
                Parent root = loader.load();


                VerObjetoAdmin controller = loader.getController();
                controller.setObjeto(objeto);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Detalle del Objeto");
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

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
