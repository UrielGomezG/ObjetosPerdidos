package com.utez.objetosperdidos.controller;

import com.utez.objetosperdidos.model.ObjetoPerdido;
import com.utez.objetosperdidos.model.dao.ObjetoDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ObjetosAdminController {

    @FXML
    private FlowPane contenedorTarjetas;

    private final ObjetoDAO dao = new ObjetoDAO();

    @FXML
    public void initialize() {
        cargarObjetos();
    }

    private void cargarObjetos() {
        contenedorTarjetas.getChildren().clear();
        List<ObjetoPerdido> objetos = dao.obtenerObjetosConInfo(0, 100);

        for (ObjetoPerdido obj : objetos) {
            VBox tarjeta = crearTarjetaAdmin(obj);
            contenedorTarjetas.getChildren().add(tarjeta);
        }
    }

    private VBox crearTarjetaAdmin(ObjetoPerdido obj) {
        VBox tarjeta = new VBox(5);
        tarjeta.setPadding(new javafx.geometry.Insets(10));
        tarjeta.setStyle("-fx-border-color: #aaa; -fx-background-color: #f8f8f8;");
        tarjeta.setPrefWidth(250);

        Label lblNombre = new Label("📦 " + obj.getNombreObjeto());
        Label lblDescripcion = new Label(obj.getDescripcion());
        Label lblUbicacion = new Label("📍 " + obj.getEdificio() + " - Aula " + obj.getAula());
        Label lblEstado = new Label("Estado: " + obj.getEstado());

        Button btnEditar = new Button("✏️ Editar");
        Button btnEliminar = new Button("❌ Eliminar");
        Button btnEntregar = new Button("📦 Marcar como entregado");

        lblDescripcion.setWrapText(true);

        btnEditar.setOnAction(e -> abrirEdicion(obj));
        btnEliminar.setOnAction(e -> eliminarObjeto(obj));
        btnEntregar.setOnAction(e -> abrirEntrega(obj));

        tarjeta.getChildren().addAll(lblNombre, lblDescripcion, lblUbicacion, lblEstado,
                btnEditar, btnEliminar, btnEntregar);

        return tarjeta;
    }

    private void abrirEdicion(ObjetoPerdido obj) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/utez/objetosperdidos/view/Editar_Objeto.fxml"));
            Parent root = loader.load();

            EditarObjetoController controller = loader.getController();
            controller.setObjeto(obj);

            Stage modal = new Stage();
            modal.setTitle("Editar Objeto");
            modal.setScene(new Scene(root));
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.showAndWait();

            cargarObjetos(); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void eliminarObjeto(ObjetoPerdido obj) {
        boolean confirmado = mostrarConfirmacion("¿Eliminar el objeto '" + obj.getNombreObjeto() + "'?");
        if (confirmado && dao.eliminarObjeto(obj.getId())) {
            mostrarInfo("Objeto eliminado.");
            cargarObjetos();
        }
    }

    private void abrirEntrega(ObjetoPerdido obj) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/utez/objetosperdidos/view/Entregar_Objeto.fxml"));
            Parent root = loader.load();

            EntregarObjetoController controller = loader.getController();
            controller.setObjeto(obj);

            Stage modal = new Stage();
            modal.setTitle("Registrar Entrega");
            modal.setScene(new Scene(root));
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.showAndWait();

            cargarObjetos();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean mostrarConfirmacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        return alert.showAndWait().filter(btn -> btn == ButtonType.OK).isPresent();
    }

    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
