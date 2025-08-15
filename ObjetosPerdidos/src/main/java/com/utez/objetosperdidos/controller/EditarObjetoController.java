package com.utez.objetosperdidos.controller;

import com.utez.objetosperdidos.model.Categoria;
import com.utez.objetosperdidos.model.Edificio;
import com.utez.objetosperdidos.model.ObjetoPerdido;
import com.utez.objetosperdidos.model.dao.CategoriaDAO;
import com.utez.objetosperdidos.model.dao.EdificioDAO;
import com.utez.objetosperdidos.model.dao.ObjetoDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class EditarObjetoController {

    @FXML private TextField tituloField;
    @FXML private TextField modeloField;
    @FXML private TextField marcaField;
    @FXML private TextField noSerieField;
    @FXML private TextField aulaField;
    @FXML private TextField fotoUrlField;
    @FXML private ComboBox<Categoria> cbCategoria;
    @FXML private ComboBox<Edificio> cbEdificio;
    @FXML private TextArea descripcionArea;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;
    @FXML private ImageView imgPreview;

    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final EdificioDAO edificioDAO = new EdificioDAO();
    private final ObjetoDAO objetoDAO = new ObjetoDAO();

    private ObjetoPerdido objetoEditando;
    private String nombreArchivoImagen;

    @FXML
    public void initialize() {
        cbEdificio.setItems(FXCollections.observableArrayList(edificioDAO.obtenerTodos()));
        cbCategoria.setItems(FXCollections.observableArrayList(categoriaDAO.obtenerCategorias()));
    }

    public void setObjetoEditando(ObjetoPerdido objeto) {
        this.objetoEditando = objeto;

        if (objeto != null) {
            tituloField.setText(objeto.getNombreObjeto());
            modeloField.setText(objeto.getModelo());
            marcaField.setText(objeto.getMarca());
            noSerieField.setText(objeto.getNo_serial());
            aulaField.setText(objeto.getAula());
            fotoUrlField.setText(objeto.getFotoUrl());
            descripcionArea.setText(objeto.getDescripcion());

            cbCategoria.setItems(FXCollections.observableArrayList(categoriaDAO.obtenerCategorias()));
            cbCategoria.setValue(objeto.getCategoria());

            cbEdificio.setItems(FXCollections.observableArrayList(edificioDAO.obtenerTodos()));
            for (Edificio e : edificioDAO.obtenerTodos()) {
                if (e.getId() == objeto.getEdificioId()) {
                    cbEdificio.setValue(e);
                    break;
                }
            }

            Path rutaImagen = Paths.get(System.getProperty("user.home"), "Downloads", objeto.getFotoUrl());
            if (Files.exists(rutaImagen)) {
                imgPreview.setImage(new Image(rutaImagen.toUri().toString()));
            }
        }
    }

    @FXML
    public void handleSubirImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"));

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
                imgPreview.setImage(new Image(destino.toUri().toString()));
            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlerta("Ocurrió un error al copiar la imagen.");
            }
        }
    }

    @FXML
    public void cancelar() {
        ((Stage) btnCancelar.getScene().getWindow()).close();
    }

    @FXML
    public void guardarCambios() {
        Categoria categoriaSelect = cbCategoria.getValue();
        Edificio edificioSeleccionado = cbEdificio.getValue();

        if (tituloField.getText().isEmpty() || modeloField.getText().isEmpty() || marcaField.getText().isEmpty()
                || noSerieField.getText().isEmpty() || aulaField.getText().isEmpty()
                || descripcionArea.getText().isEmpty() || categoriaSelect == null || edificioSeleccionado == null) {
            mostrarAlerta("Por favor completa todos los campos antes de guardar.");
            return;
        }

        objetoEditando.setNombreObjeto(tituloField.getText());
        objetoEditando.setModelo(modeloField.getText());
        objetoEditando.setMarca(marcaField.getText());
        objetoEditando.setNo_serial(noSerieField.getText());
        objetoEditando.setAula(aulaField.getText());
        objetoEditando.setDescripcion(descripcionArea.getText());
        objetoEditando.setFotoUrl(fotoUrlField.getText());
        objetoEditando.setCategoria(categoriaSelect);
        objetoEditando.setEdificioId(edificioSeleccionado.getId());
        objetoEditando.setEdificio(edificioSeleccionado.getNombre());

        if (objetoDAO.actualizarObjeto(objetoEditando)) {
            ((Stage) btnGuardar.getScene().getWindow()).close();
        } else {
            mostrarAlerta("No se pudo actualizar el objeto.");
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
