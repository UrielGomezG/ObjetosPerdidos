package com.utez.objetosperdidos.controller;

import com.utez.objetosperdidos.model.ObjetoPerdido;
import com.utez.objetosperdidos.model.dao.ObjetoDAO;
import com.utez.objetosperdidos.util.Session;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.control.Label;

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

    private List<ObjetoPerdido> objetos;
    private final ObjetoDAO dao = new ObjetoDAO();

    @FXML
    public void initialize() {
        objetos = dao.obtenerObjetosEntregados()
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
        System.out.println("Mostrando objetos: " + lista.size());
        itemsContainer.getChildren().clear();
        for (ObjetoPerdido o : lista) {
            VBox card = crearTarjetaHistorial(o);
            System.out.println("Agregando tarjeta para: " + o.getNombreObjeto());
            itemsContainer.getChildren().add(card);
        }
    }

    private VBox crearTarjetaHistorial(ObjetoPerdido obj) {
        VBox tarjeta = new VBox(10);
        tarjeta.setPadding(new Insets(10));
        tarjeta.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-border-radius: 8; "
                + "-fx-border-color: #ccc; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0.3, 0, 2);");
        tarjeta.setPrefWidth(270);

        // 🔍 Imagen del objeto
        ImageView imagenObjeto = new ImageView();
        imagenObjeto.setFitWidth(80);
        imagenObjeto.setFitHeight(80);
        imagenObjeto.setPreserveRatio(true);
        imagenObjeto.setSmooth(true);

        if (obj.getFotoUrl() != null && !obj.getFotoUrl().trim().isEmpty()) {
            Path rutaImagen = Paths.get(System.getProperty("user.home"),  "Downloads", obj.getFotoUrl().trim());
            if (Files.exists(rutaImagen)) {
                imagenObjeto.setImage(new Image(rutaImagen.toUri().toString()));
                System.out.println("Imagen cargada: " + rutaImagen.getFileName());
            } else {
                System.out.println("Imagen no encontrada: " + rutaImagen.toAbsolutePath());
            }
        } else {
            System.out.println("ℹNo se proporcionó imagen para: " + obj.getNombreObjeto());
        }

        // 📄 Información textual
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

        Button btnVer = new Button("👁️ Ver");



        VBox botones = new VBox(5, btnVer);
        botones.setPadding(new Insets(5, 0, 0, 0));

        tarjeta.getChildren().addAll(encabezado, lblDescripcion, botones);
        return tarjeta;
    }

    private void abrirDetalles(ObjetoPerdido obj) {
        System.out.println("Ver detalles de: " + obj.getNombreObjeto());
        // Aquí puedes abrir ventana o modal con detalles
    }
}
