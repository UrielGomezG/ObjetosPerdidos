package com.utez.objetosperdidos.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import com.utez.objetosperdidos.model.ObjetoPerdido;
import com.utez.objetosperdidos.model.EntregaObjeto;
import com.utez.objetosperdidos.model.dao.ObjetoDAO;
import com.utez.objetosperdidos.model.dao.EntregarObjetoDao;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class EntregarObjetoController {

    @FXML private Label lblTitulo;
    @FXML private Label lblMarca;
    @FXML private Label lblModelo;
    @FXML private Label lblNoSerie;
    @FXML private Label lblDescripcion;
    @FXML private TextField txtAlumno;
    @FXML private TextField txtMatricula;
    @FXML private Label lblAula;
    @FXML private Label lblEdificio;
    @FXML private Label lblNombreFoto;
    @FXML private TextField fotoUrlField;
    @FXML private ImageView imgPreview;

    private String nombreArchivoImagen;
    private ObjetoPerdido objeto;
    private final ObjetoDAO dao = new ObjetoDAO();

    public void initialize() { }

    public void setObjeto(ObjetoPerdido objeto) {
        this.objeto = objeto;
        if(objeto != null){
            lblTitulo.setText(objeto.getNombreObjeto());
            lblMarca.setText(objeto.getMarca());
            lblModelo.setText(objeto.getModelo());
            fotoUrlField.setText(objeto.getFotoUrl());
            lblNoSerie.setText(objeto.getNo_serial());
            lblDescripcion.setText(objeto.getDescripcion());
            lblAula.setText(objeto.getAula());
            lblEdificio.setText(objeto.getEdificio());
        }
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

        // 1️⃣ Crear objeto de entrega
        EntregaObjeto entrega = new EntregaObjeto(
                objeto.getId(),
                fotoUrlField.getText().trim(),
                new Date(),
                nombreIngresado,
                matriculaIngresada
        );

        // 2️⃣ Registrar entrega en la tabla ENTREGAS_OBJETO
        EntregarObjetoDao entregaDao = new EntregarObjetoDao();
        boolean entregaOk = entregaDao.registrarEntrega(entrega);

        // 3️⃣ Marcar objeto como entregado (estado 21)
        boolean estadoOk = dao.cambiarEstadoObjeto(objeto.getId(), 21);

        Alert alert;
        if (entregaOk && estadoOk) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Entrega registrada correctamente al alumno: "
                    + nombreIngresado + " (Matrícula: " + matriculaIngresada + ")");
            alert.showAndWait();
            ((Stage) lblTitulo.getScene().getWindow()).close();
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("No se pudo registrar la entrega correctamente.");
            alert.showAndWait();
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
