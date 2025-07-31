package com.utez.objetosperdidos.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.utez.objetosperdidos.model.ObjetoPerdido;
import com.utez.objetosperdidos.model.User;
import com.utez.objetosperdidos.model.dao.ObjetoDAO;
import com.utez.objetosperdidos.model.dao.UserDAO;

import javafx.event.ActionEvent;
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
    private ObjetoPerdido objetoEditando;

    private ObjetoPerdido objeto;

    private final ObjetoDAO dao = new ObjetoDAO();

    private final UserDAO userDAO = new UserDAO();

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
        if (txtAlumno.getText().trim().isEmpty() || txtMatricula.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Por favor, complete todos los campos del alumno.");
            alert.showAndWait();
            return;
        }

        // Validar que el alumno exista en la base de datos
        String matricula = txtMatricula.getText().trim();
        User alumno = userDAO.obtenerAlumnoPorMatricula(matricula);
        
        if (alumno == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("La matrícula ingresada no corresponde a un alumno registrado en el sistema.");
            alert.showAndWait();
            return;
        }

        // Validar que el nombre ingresado coincida con el alumno encontrado
        String nombreCompletoAlumno = alumno.getName() + " " + alumno.getApellidoPaterno() + " " + alumno.getApellidoMaterno();
        String nombreIngresado = txtAlumno.getText().trim();
        
        if (!nombreCompletoAlumno.toLowerCase().contains(nombreIngresado.toLowerCase()) && 
            !nombreIngresado.toLowerCase().contains(alumno.getName().toLowerCase())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("El nombre ingresado no coincide con el alumno registrado con esa matrícula.");
            alert.showAndWait();
            return;
        }

        // Marcar el objeto como entregado
        boolean ok = dao.marcarComoEntregadoPorAlumno(objeto.getId(), alumno.getId() );

        Alert alert = new Alert(ok ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(ok ? "Objeto marcado como entregado al alumno: " + nombreCompletoAlumno : "No se pudo registrar la entrega.");
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
