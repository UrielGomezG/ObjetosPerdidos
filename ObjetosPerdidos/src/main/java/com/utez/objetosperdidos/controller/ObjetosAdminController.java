package com.utez.objetosperdidos.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.utez.objetosperdidos.model.ObjetoPerdido;
import com.utez.objetosperdidos.model.dao.ObjetoDAO;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ObjetosAdminController {

    @FXML
    private FlowPane contenedorTarjetas;

    @FXML
    private TextField txtBuscar;

    @FXML
    private Button btnBuscar;

    private final ObjetoDAO dao = new ObjetoDAO();
    private List<ObjetoPerdido> listaOriginal;

    @FXML
    public void initialize() {
        if (contenedorTarjetas == null) {
            System.out.println("❌ Error: contenedorTarjetas no está vinculado. Revisa el fx:id en el FXML.");
        } else {
            System.out.println("✅ Inicializando vista de objetos perdidos...");
        }

        listaOriginal = dao.obtenerObjetosConInfo(0, 100);
        mostrarObjetos(listaOriginal);

        if (btnBuscar != null) {
            btnBuscar.setOnAction(e -> buscarPorNombre(txtBuscar.getText()));
        }

        if (txtBuscar != null) {
            txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> buscarPorNombre(newVal));
        }
    }

    private void buscarPorNombre(String filtro) {
        if (filtro == null || filtro.isBlank()) {
            mostrarObjetos(listaOriginal);
            return;
        }

        List<ObjetoPerdido> filtrados = listaOriginal.stream()
            .filter(obj -> obj.getNombreObjeto().toLowerCase().contains(filtro.toLowerCase()))
            .toList();

        mostrarObjetos(filtrados);
    }

    private void mostrarObjetos(List<ObjetoPerdido> objetos) {
        contenedorTarjetas.getChildren().clear();

        for (ObjetoPerdido obj : objetos) {
            VBox tarjeta = crearTarjetaAdmin(obj);
            contenedorTarjetas.getChildren().add(tarjeta);
        }
    }

    private VBox crearTarjetaAdmin(ObjetoPerdido obj) {
        VBox tarjeta = new VBox(10);
        tarjeta.setPadding(new Insets(10));
        tarjeta.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-border-radius: 8; "
                + "-fx-border-color: #ccc; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0.3, 0, 2);");
        tarjeta.setPrefWidth(270);

        ImageView imagenObjeto = new ImageView();
        imagenObjeto.setFitWidth(80);
        imagenObjeto.setFitHeight(80);
        imagenObjeto.setPreserveRatio(true);
        imagenObjeto.setSmooth(true);

        if (obj.getFotoUrl() != null && !obj.getFotoUrl().trim().isEmpty()) {
            Path rutaImagen = Paths.get(System.getProperty("user.home"), "Downloads", obj.getFotoUrl().trim());
            if (Files.exists(rutaImagen)) {
                imagenObjeto.setImage(new Image(rutaImagen.toUri().toString()));
                System.out.println("Imagen cargada: " + rutaImagen.getFileName());
            } else {
                System.out.println("Imagen no encontrada: " + rutaImagen.toAbsolutePath());
            }
        } else {
            System.out.println("No se proporcionó imagen para: " + obj.getNombreObjeto());
        }

        VBox infoTexto = new VBox(4);
        infoTexto.getChildren().addAll(
                new Label("📦 " + obj.getNombreObjeto()),
                new Label("📍 " + obj.getEdificio() + " - Aula " + obj.getAula()),
                new Label("🔖 Estado: " + obj.getEstado())
        );

        HBox encabezado = new HBox(12);
        encabezado.setAlignment(Pos.CENTER_LEFT);
        encabezado.getChildren().addAll(imagenObjeto, infoTexto);

        Label lblDescripcion = new Label(obj.getDescripcion());
        lblDescripcion.setWrapText(true);

        Button btnEditar = new Button("✏️ Editar");
        Button btnEliminar = new Button("❌ Eliminar");
        Button btnEnviarBodega = new Button("🏢 Enviar a Bodega");
        Button btnEntregar = new Button("📦 Marcar como entregado");

        btnEditar.setOnAction(e -> abrirEdicion(obj));
        btnEliminar.setOnAction(e -> eliminarObjeto(obj));
        btnEnviarBodega.setOnAction(e -> enviarABodega(obj));
        btnEntregar.setOnAction(e -> abrirEntrega(obj));

        VBox botones = new VBox(5, btnEditar, btnEliminar, btnEnviarBodega, btnEntregar);
        botones.setPadding(new Insets(5, 0, 0, 0));

        tarjeta.getChildren().addAll(encabezado, lblDescripcion, botones);
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

            mostrarObjetos(listaOriginal);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void eliminarObjeto(ObjetoPerdido obj) {
        boolean confirmado = mostrarConfirmacion("¿Eliminar el objeto '" + obj.getNombreObjeto() + "'?");
        if (confirmado && dao.eliminarObjeto(obj.getId())) {
            mostrarInfo("Objeto eliminado.");
            listaOriginal = dao.obtenerObjetosConInfo(0, 100);
            mostrarObjetos(listaOriginal);
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

            listaOriginal = dao.obtenerObjetosConInfo(0, 100);
            mostrarObjetos(listaOriginal);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void enviarABodega(ObjetoPerdido obj) {
        boolean confirmado = mostrarConfirmacion("¿Enviar el objeto '" + obj.getNombreObjeto() + "' a bodega?");
        if (confirmado) {
            if (dao.enviarObjetoABodega(obj.getId())) {
                mostrarInfo("Objeto enviado a bodega correctamente.");
                listaOriginal = dao.obtenerObjetosConInfo(0, 100);
                mostrarObjetos(listaOriginal);
            } else {
                mostrarError("Error al enviar el objeto a bodega.");
            }
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

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
