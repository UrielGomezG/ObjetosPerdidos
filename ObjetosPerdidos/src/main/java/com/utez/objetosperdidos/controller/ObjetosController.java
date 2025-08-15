package com.utez.objetosperdidos.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.utez.objetosperdidos.model.Categoria;
import com.utez.objetosperdidos.model.ObjetoPerdido;
import com.utez.objetosperdidos.model.dao.ObjetoDAO;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ObjetosController {

    @FXML
    private FlowPane contenedorTarjetas;
    @FXML
    private ComboBox<String> cbCategoria;
    @FXML
    private TextField txtBuscar;
    @FXML
    private Button btnBuscar;

    private final ObjetoDAO dao = new ObjetoDAO();
    private List<ObjetoPerdido> listaOriginal;

    @FXML
    public void initialize() {
        cargarObjetos();
        listaOriginal = dao.obtenerObjetosConInfo(0, 100);
        mostrarObjetos(listaOriginal);
        cargarCategorias();

        btnBuscar.setOnAction(e -> buscarPorNombre(txtBuscar.getText()));
        txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> buscarPorNombre(newVal));
    }

    private void cargarCategorias() {
        cbCategoria.getItems().clear();
        cbCategoria.getItems().add("Todas");
        List<Categoria> categorias = dao.obtenerCategorias();

        for (Categoria cat : categorias) {
            cbCategoria.getItems().add(cat.getNombre());
        }

        cbCategoria.getSelectionModel().selectFirst();
        cbCategoria.setOnAction(e -> filtrarPorNombreYCategoria());
    }

    private void filtrarPorNombreYCategoria() {
        String texto = txtBuscar.getText().toLowerCase();
        String categoriaSeleccionada = cbCategoria.getValue();

        List<ObjetoPerdido> filtrados = listaOriginal.stream()
                .filter(obj -> {
                    boolean coincideTexto = obj.getNombreObjeto().toLowerCase().contains(texto);
                    boolean coincideCategoria = categoriaSeleccionada == null || categoriaSeleccionada.equals("Todas")
                            || (obj.getCategoria() != null && obj.getCategoria().getNombre().equalsIgnoreCase(categoriaSeleccionada));
                    return coincideTexto && coincideCategoria;
                })
                .toList();

        mostrarObjetos(filtrados);
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
            VBox tarjeta = crearTarjetaUsuario(obj);
            contenedorTarjetas.getChildren().add(tarjeta);
        }
    }

    private void cargarObjetos() {
        contenedorTarjetas.getChildren().clear();
        List<ObjetoPerdido> objetos = dao.obtenerObjetosConInfo(0, 100);

        for (ObjetoPerdido obj : objetos) {
            VBox tarjeta = crearTarjetaUsuario(obj);
            contenedorTarjetas.getChildren().add(tarjeta);
        }
    }

    private VBox crearTarjetaUsuario(ObjetoPerdido obj) {
        VBox tarjeta = new VBox(10);
        tarjeta.getStyleClass().add("card");
        tarjeta.setAlignment(Pos.TOP_LEFT);
        tarjeta.setPadding(new Insets(16));
        tarjeta.setPrefSize(300, 260);

        Label titulo = new Label("Objeto perdido");
        titulo.getStyleClass().add("card-title");

        ImageView imagenObjeto = new ImageView();
        imagenObjeto.getStyleClass().add("card-image");
        imagenObjeto.setFitWidth(80);
        imagenObjeto.setFitHeight(80);
        imagenObjeto.setPreserveRatio(true);
        imagenObjeto.setSmooth(true);

        if (obj.getFotoUrl() != null && !obj.getFotoUrl().trim().isEmpty()) {
            Path rutaImagen = Paths.get(System.getProperty("user.home"), "Downloads", obj.getFotoUrl().trim());
            if (Files.exists(rutaImagen)) {
                imagenObjeto.setImage(new Image(rutaImagen.toUri().toString()));
            }
        }

        VBox infoTexto = new VBox(4);
        infoTexto.getStyleClass().add("card-info");
        infoTexto.getChildren().addAll(
                new Label("📦 Nombre: " + obj.getNombreObjeto()),
                new Label("📍 Ubicación: " + obj.getEdificio() + " - Aula " + obj.getAula()),
                new Label("🏷️ Categoría: " + (obj.getCategoria() != null ? obj.getCategoria().getNombre() : "Sin categoría"))
        );

        tarjeta.setOnMouseClicked(event -> {
            if (!(event.getTarget() instanceof Button)) { // Evita que botones activen esto
                verObjetoDetalle(obj);
            }
        });

        // 🎨 Efecto hover para la tarjeta
        tarjeta.setOnMouseEntered(e -> tarjeta.setStyle(
                "-fx-background-color: #f9f9f9; -fx-background-radius: 8; -fx-border-radius: 8; "
                        + "-fx-border-color: #bbb; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 6, 0.3, 0, 2);"
        ));
        tarjeta.setOnMouseExited(e -> tarjeta.setStyle(
                "-fx-background-color: white; -fx-background-radius: 8; -fx-border-radius: 8; "
                        + "-fx-border-color: #ccc; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0.3, 0, 2);"
        ));


        HBox encabezado = new HBox(12);
        encabezado.setAlignment(Pos.CENTER_LEFT);
        encabezado.getChildren().addAll(imagenObjeto, infoTexto);

        Label lblDescripcion = new Label("📝 " + obj.getDescripcion());
        lblDescripcion.getStyleClass().add("card-description");
        lblDescripcion.setWrapText(true);

        Label estadoValor = new Label(obj.getEstado().equalsIgnoreCase("Entregado") ? "Entregado" : "Activo");
        estadoValor.getStyleClass().add(obj.getEstado().equalsIgnoreCase("Entregado") ? "entregado-label" : "activo-label");
        estadoValor.setMaxWidth(Double.MAX_VALUE);
        estadoValor.setAlignment(Pos.CENTER);

        VBox estadoBox = new VBox(estadoValor);
        estadoBox.setAlignment(Pos.BOTTOM_CENTER);
        estadoBox.setPadding(new Insets(10, 0, 0, 0));

        tarjeta.getChildren().addAll(titulo, encabezado, lblDescripcion, estadoBox);
        return tarjeta;
    }




    private void verObjetoDetalle(ObjetoPerdido obj) {
        try {
            // Cargar FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/utez/objetosperdidos/view/Ver_Objeto.fxml"));
            Parent root = loader.load();

            // Obtener el controlador correctamente desde el FXMLLoader
            VerObjeto controller = loader.getController();
            controller.setObjeto(obj); // <-- pasar el objeto al controlador

            // Crear la nueva ventana
            Stage stage = new Stage();
            stage.setTitle("Ver Objeto: " + obj.getNombreObjeto());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al abrir la vista de ver objeto.");
        }
    }

}