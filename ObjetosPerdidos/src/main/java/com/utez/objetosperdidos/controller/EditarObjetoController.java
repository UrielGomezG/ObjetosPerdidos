package com.utez.objetosperdidos.controller;

import com.utez.objetosperdidos.model.ObjetoPerdido;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class EditarObjetoController {

    @FXML
    private TextField tituloField;

    @FXML
    private TextField aulaField;

    @FXML
    private TextField fotoUrlField;

    @FXML
    private TextArea descripcionArea;

    @FXML
    private ImageView imgPreview;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnGuardar;

    private String nombreArchivoImagen;
    private ObjetoPerdido objetoEditando;


    public void setObjeto(ObjetoPerdido obj) {
        this.objetoEditando = obj;

        if (objetoEditando != null) {
            tituloField.setText(objetoEditando.getNombreObjeto());
            aulaField.setText(objetoEditando.getAula());
            fotoUrlField.setText(objetoEditando.getFotoUrl());
            descripcionArea.setText(objetoEditando.getDescripcion());

            Path rutaImagen = Paths.get(System.getProperty("user.home"), "objetos-imagenes", objetoEditando.getFotoUrl());
            if (Files.exists(rutaImagen)) {
                Image imagen = new Image(rutaImagen.toUri().toString());
                imgPreview.setImage(imagen);
            } else {
                System.out.println("⚠️ No se encontró imagen: " + rutaImagen.toAbsolutePath());
            }
        }
    }

    @FXML
    public void handleSubirImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        File archivoSeleccionado = fileChooser.showOpenDialog(new Stage());
        if (archivoSeleccionado != null) {
            nombreArchivoImagen = archivoSeleccionado.getName();

            String nombre = nombreArchivoImagen.toLowerCase();
            if (!(nombre.endsWith(".png") || nombre.endsWith(".jpg") || nombre.endsWith(".jpeg"))) {
                mostrarAlerta("El archivo seleccionado no es una imagen válida.");
                return;
            }

            fotoUrlField.setText(nombreArchivoImagen);

            Path destino = Paths.get(System.getProperty("user.home"), "objetos-imagenes", nombreArchivoImagen);
            try {
                Files.createDirectories(destino.getParent());
                Files.copy(archivoSeleccionado.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("✅ Imagen copiada: " + destino);

                Image imagen = new Image(destino.toUri().toString());
                imgPreview.setImage(imagen);
            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlerta("Ocurrió un error al copiar la imagen.");
            }
        }
    }

    @FXML
    public void cancelar() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void guardarCambios() {
        if (tituloField.getText().isEmpty() || aulaField.getText().isEmpty() || descripcionArea.getText().isEmpty()) {
            mostrarAlerta("Por favor completa todos los campos antes de guardar.");
            return;
        }

        objetoEditando.setNombreObjeto(tituloField.getText());
        objetoEditando.setAula(aulaField.getText());
        objetoEditando.setDescripcion(descripcionArea.getText());
        objetoEditando.setFotoUrl(fotoUrlField.getText());

        // Aquí podrías llamar a ObjetoDAO.actualizarObjeto(objetoEditando)
        System.out.println("💾 Cambios guardados en objeto: " + objetoEditando.getNombreObjeto());

        Stage stage = (Stage) btnGuardar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
