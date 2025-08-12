package com.utez.objetosperdidos.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.utez.objetosperdidos.model.Categoria;
import com.utez.objetosperdidos.model.ObjetoPerdido;
import com.utez.objetosperdidos.model.dao.ObjetoDAO;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
        if (contenedorTarjetas == null) {
            System.out.println("Error: contenedorTarjetas no está vinculado. Revisa el fx:id en el FXML.");
        } else {
            System.out.println("Inicializando vista de objetos perdidos para usuario...");
        }
        cargarObjetos();
        listaOriginal = dao.obtenerObjetosConInfo(0, 100);
        mostrarObjetos(listaOriginal);
        cargarCategorias();
        if (btnBuscar != null) {
            btnBuscar.setOnAction(e -> buscarPorNombre(txtBuscar.getText()));
        }

        if (txtBuscar != null) {
            txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> buscarPorNombre(newVal));
        }
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
        tarjeta.setPadding(new Insets(10));
        tarjeta.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-border-radius: 8; "
                + "-fx-border-color: #ccc; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0.3, 0, 2);");
        tarjeta.setPrefWidth(270);

        // Imagen del objeto
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
            System.out.println("ℹNo se proporcionó imagen para: " + obj.getNombreObjeto());
        }

        // Información textual
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

        // Solo mostrar la información, sin botones de modificación
        tarjeta.getChildren().addAll(encabezado, lblDescripcion);
        return tarjeta;
    }
}
