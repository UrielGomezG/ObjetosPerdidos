package com.utez.objetosperdidos.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import com.utez.objetosperdidos.model.EntregaObjeto;
import com.utez.objetosperdidos.model.ObjetoPerdido;
import com.utez.objetosperdidos.model.dao.EntregarObjetoDao;
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
    private Label lblAula;
    @FXML
    private Label lblEdificio;
    @FXML
    private Label lblNombreFoto;
    @FXML
    private TextField fotoUrlField;
    @FXML
    private ImageView imgPreview;

    private String nombreArchivoImagen;
    private ObjetoPerdido objeto;

    private final ObjetoDAO dao = new ObjetoDAO();
    private final EntregarObjetoDao entregaDao = new EntregarObjetoDao();

    public void initialize() {}

    public void setObjeto(ObjetoPerdido objeto) {
        this.objeto = objeto;

        lblTitulo.setText(objeto.getNombreObjeto());
        lblMarca.setText(objeto.getMarca());
        lblModelo.setText(objeto.getModelo());
        fotoUrlField.setText(objeto.getFotoUrl());
        lblNoSerie.setText(objeto.getNo_serial());
        lblDescripcion.setText(objeto.getDescripcion());
        lblAula.setText(objeto.getAula());
        lblEdificio.setText(objeto.getEdificio());
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

            if (!nombreArchivoImagen.toLowerCase().matches(".*\\.(png|jpg|jpeg)$")) {
                mostrarAlerta("El archivo seleccionado no es una imagen válida.");
                return;
            }

            fotoUrlField.setText(nombreArchivoImagen);

            Path destino = Paths.get(System.getProperty("user.home"), "Downloads", nombreArchivoImagen);
            try {
                Files.createDirectories(destino.getParent());
                Files.copy(archivoSeleccionado.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);

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
        String nombreIngresado = txtAlumno.getText().trim();
        String matriculaIngresada = txtMatricula.getText().trim();

        if (nombreIngresado.isEmpty() || matriculaIngresada.isEmpty()) {
            mostrarAlerta("Por favor, complete todos los campos del alumno.");
            return;
        }

        if (!nombreIngresado.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+")) {
            mostrarAlerta("El nombre solo debe contener letras y espacios.");
            return;
        }

        if (!matriculaIngresada.matches("[a-zA-Z0-9]+")) {
            mostrarAlerta("La matrícula solo debe contener letras y números.");
            return;
        }

        boolean ok = dao.cambiarEstadoObjeto(objeto.getId(), 21);
        if (!ok) {
            mostrarAlerta("No se pudo actualizar el estado del objeto.");
            return;
        }

        EntregaObjeto entrega = new EntregaObjeto(
                objeto.getId(),
                fotoUrlField.getText().trim(),
                new Date()
        );

        boolean guardado = entregaDao.registrarEntrega(entrega);
        if (!guardado) {
            mostrarAlerta("La entrega no pudo guardarse en la base de datos.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Objeto entregado correctamente.");
        alert.showAndWait();

        ((Stage) lblTitulo.getScene().getWindow()).close();
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
