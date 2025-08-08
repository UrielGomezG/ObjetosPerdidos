package com.utez.objetosperdidos.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.utez.objetosperdidos.model.ObjetoPerdido;
import com.utez.objetosperdidos.model.dao.ObjetoDAO;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class EntregarObjetoController {

    @FXML
    private Label lblTitulo;
    @FXML
    private Label lblMarca;
    @FXML
    private Label lblModelo;
    @FXML
    private Label lblNoSerie;
    @FXML
    private Label lblDescripcion;
    @FXML
    private TextField txtAlumno;
    @FXML
    private TextField txtMatricula;

    @FXML
    private Label lblNombreFoto;

    @FXML
    private TextField fotoUrlField;

    @FXML
    private ImageView imgPreview;
    private String nombreArchivoImagen;
    private ObjetoPerdido objeto;

    private final ObjetoDAO dao = new ObjetoDAO();

    public void setObjeto(ObjetoPerdido objeto) {
        this.objeto = objeto;
        lblTitulo.setText(objeto.getNombreObjeto());
        lblMarca.setText(objeto.getMarca());
        lblModelo.setText(objeto.getModelo());
        fotoUrlField.setText(objeto.getFotoUrl());
        lblNoSerie.setText(objeto.getNo_serial());
        lblDescripcion.setText(objeto.getDescripcion());
    }

    @FXML
    public void handleSubirImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"));

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
                System.out.println("Imagen copiada: " + destino);

                Image imagen = new Image(destino.toUri().toString());
                imgPreview.setImage(imagen);
            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlerta("Ocurrió un error al copiar la imagen.");
            }
        }
    }

    @FXML
    public void confirmarEntrega() {
        // Validar que se ingresen los datos del alumno
        String nombreIngresado = txtAlumno.getText().trim();
        String matriculaIngresada = txtMatricula.getText().trim();

        if (nombreIngresado.isEmpty() || matriculaIngresada.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Por favor, complete todos los campos del alumno.");
            alert.showAndWait();
            return;
        }

        // Validar que el nombre solo contenga letras (puede incluir espacios y acentos)
        if (!nombreIngresado.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\s]+")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("El nombre solo debe contener letras y espacios, sin caracteres especiales ni números.");
            alert.showAndWait();
            return;
        }

        // Validar que la matrícula solo contenga letras y números
        if (!matriculaIngresada.matches("[a-zA-Z0-9]+")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("La matrícula solo debe contener letras y números, sin caracteres especiales.");
            alert.showAndWait();
            return;
        }

        // Marcar el objeto como entregado (cambiando el estado a 21)
        boolean ok = dao.cambiarEstadoObjeto(objeto.getId(), 21);

        Alert alert = new Alert(ok ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(ok ? "Objeto marcado como entregado al alumno: " + nombreIngresado + " (Matrícula: " + matriculaIngresada + ")" : "No se pudo registrar la entrega.");
        alert.showAndWait();

        if (ok) {
            ((Stage) lblTitulo.getScene().getWindow()).close();
        }
    }

    @FXML
    public void cancelar() {
        ((Stage) lblTitulo.getScene().getWindow()).close();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
