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

    @FXML
    private TextField tituloField;

    @FXML
    private TextField aulaField;

    @FXML
    private TextField fotoUrlField;

    @FXML
    private ComboBox<Categoria> cbCategoria;

    @FXML
    private ComboBox<Edificio> cbEdificio;

    //@FXML
    //private ComboBox<Estado> cbEstado;

    @FXML
    private TextArea descripcionArea;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnCancelar;

    @FXML
    private ImageView imgPreview;

    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    //private final EstadoDAO estadoDAO = new EstadoDAO();
    private final EdificioDAO edificioDAO = new EdificioDAO();
    private final ObjetoDAO objetoDAO = new ObjetoDAO();

    private ObjetoPerdido objetoEditando;
    private String nombreArchivoImagen;

    @FXML
    public void initialize() {
        List<Edificio> edificios = edificioDAO.obtenerTodos();
        cbEdificio.setItems(FXCollections.observableArrayList(edificios));
        List<Categoria> categorias = categoriaDAO.obtenerCategorias();
        cbCategoria.setItems(FXCollections.observableArrayList(categorias));
    }


    public void setObjetoEditando(ObjetoPerdido objeto) {
        this.objetoEditando = objeto;

        if (objetoEditando != null) {
            tituloField.setText(objetoEditando.getNombreObjeto());
            aulaField.setText(objetoEditando.getAula());
            fotoUrlField.setText(objetoEditando.getFotoUrl());
            descripcionArea.setText(objetoEditando.getDescripcion());
            List<Categoria> categorias = categoriaDAO.obtenerCategorias();
            cbCategoria.setItems(FXCollections.observableArrayList(categorias));
            cbCategoria.setValue(objetoEditando.getCategoria());

            //List<Estado> estados = estadoDAO.obtenerTodos();
            //cbEstado.setItems(FXCollections.observableArrayList(estados));

            List<Edificio> edificios = edificioDAO.obtenerTodos();
            cbEdificio.setItems(FXCollections.observableArrayList(edificios));

           /*  for (Estado e : estados) {
                if (e.getId() == objetoEditando.getEstadoId()) {
                    cbEstado.setValue(e);
                    break;
                }
            }*/

            for (Edificio e : edificios) {
                if (e.getId() == objetoEditando.getEdificioId()) {
                    cbEdificio.setValue(e);
                    break;
                }
            }

            Path rutaImagen = Paths.get(System.getProperty("user.home"), "objetos-imagenes", objetoEditando.getFotoUrl());
            if (Files.exists(rutaImagen)) {
                Image imagen = new Image(rutaImagen.toUri().toString());
                imgPreview.setImage(imagen);
            } else {
                System.out.println("No se encontró imagen: " + rutaImagen.toAbsolutePath());
            }
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
    public void cancelar() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void guardarCambios() {
        Categoria categoriaSelect = cbCategoria.getValue();
        //Estado estadoSeleccionado = cbEstado.getValue();
        Edificio edificioSeleccionado = cbEdificio.getValue();

        if (tituloField.getText().isEmpty() || aulaField.getText().isEmpty()
                || descripcionArea.getText().isEmpty() || categoriaSelect == null || edificioSeleccionado == null) {
            mostrarAlerta("Por favor completa todos los campos antes de guardar.");
            return;
        }

        objetoEditando.setNombreObjeto(tituloField.getText());
        objetoEditando.setAula(aulaField.getText());
        objetoEditando.setDescripcion(descripcionArea.getText());
        objetoEditando.setFotoUrl(fotoUrlField.getText());
        objetoEditando.setCategoria(categoriaSelect);
        //objetoEditando.setEstadoId(estadoSeleccionado.getId());
        //objetoEditando.setEstado(estadoSeleccionado.getNombre());
        objetoEditando.setEdificioId(edificioSeleccionado.getId());
        objetoEditando.setEdificio(edificioSeleccionado.getNombre());
        
        boolean actualizado = objetoDAO.actualizarObjeto(objetoEditando);
        if (actualizado) {
            System.out.println("Cambios guardados en objeto: " + objetoEditando.getNombreObjeto());
            Stage stage = (Stage) btnGuardar.getScene().getWindow();
            stage.close();
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
