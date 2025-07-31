package com.utez.objetosperdidos.controller;

import com.utez.objetosperdidos.model.Categoria;
import com.utez.objetosperdidos.model.Edificio;
import com.utez.objetosperdidos.model.Estado;
import com.utez.objetosperdidos.util.ConexionOracle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.nio.file.Path;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import java.time.LocalDate;

public class RegistrarObjetoController {

    @FXML
    private TextField txtTitulo;
    @FXML
    private TextField txtMarca;
    @FXML
    private TextField txtModelo;
    @FXML
    private TextField txtNoSerial;
    @FXML
    private TextArea txtDescripcion;
    @FXML
    private DatePicker dpFechaReporte;
    @FXML
    private TextField txtAula;
    @FXML
    private ComboBox<Edificio> cbEdificio;
    @FXML
    private ComboBox<Estado> cbEstado;
    @FXML
    private ComboBox<Categoria> comboCategoria;
    @FXML
    private Label lblNombreFoto;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnGuardar;

    private File archivoSeleccionado;

    @FXML
    public void initialize() {
        cargarEdificios();
        cargarEstados();
        cargarCategorias();
    }

    @FXML
    void seleccionarFoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen del objeto");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.jpeg", "*.png"));

        File archivo = fileChooser.showOpenDialog(null);
        if (archivo != null) {
            try {
                File carpetaDestino = new File("src/main/resources/com/utez/objetosperdidos/imagenes");
                if (!carpetaDestino.exists())
                    carpetaDestino.mkdirs();

                String nombreArchivo = archivo.getName();
                Path destino = carpetaDestino.toPath().resolve(nombreArchivo);

                if (Files.exists(destino)) {
                    mostrarAlerta("Archivo duplicado",
                            "Ya existe una imagen con el mismo nombre.\nRenómbrala y vuelve a intentarlo.",
                            Alert.AlertType.WARNING);
                    return;
                }

                Files.copy(archivo.toPath(), destino);
                lblNombreFoto.setText(nombreArchivo);
                archivoSeleccionado = destino.toFile();
                System.out.println("Imagen copiada a: " + destino);

            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlerta("Error", "No se pudo copiar la imagen.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void guardarObjeto(ActionEvent event) {
        String titulo = txtTitulo.getText();
        String marca = txtMarca.getText();
        String modelo = txtModelo.getText();
        String noSerial = txtNoSerial.getText();
        String descripcion = txtDescripcion.getText();
        LocalDate fechaReporte = dpFechaReporte.getValue();
        String aula = txtAula.getText();
        Edificio edificio = cbEdificio.getValue();
        Estado estado = cbEstado.getValue();
        String fotoUrl = lblNombreFoto.getText().isEmpty() ? null : lblNombreFoto.getText();

        if (titulo.isEmpty() || descripcion.isEmpty() || fechaReporte == null || edificio == null || estado == null) {
            mostrarAlerta("Campos obligatorios faltantes", "Por favor completa todos los campos obligatorios.",
                    Alert.AlertType.WARNING);
            return;
        }

        try (Connection conn = ConexionOracle.getConnection()) {
            String sql = "INSERT INTO OBJETOS_PERDIDOS (NOMBRE_EN_OBJETO, DESCRIPCION, FECHA_REPORTE, FOTO_URL, AULA, EDIFICIO_ID, ESTADO_ID, MARCA, MODELO, NO_SERIAL, ADMINISTRADOR_ID) "
                    +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement stmt = conn.prepareStatement(sql, new String[] { "ID" });
            stmt.setString(1, titulo);
            stmt.setString(2, descripcion);
            stmt.setDate(3, Date.valueOf(fechaReporte));
            stmt.setString(4, fotoUrl);
            stmt.setString(5, aula);
            stmt.setInt(6, edificio.getId());
            stmt.setInt(7, estado.getId());
            stmt.setString(8, marca);
            stmt.setString(9, modelo);
            stmt.setString(10, noSerial);
            stmt.setInt(11, 1);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int objetoId = generatedKeys.getInt(1);

                    Categoria seleccionada = comboCategoria.getSelectionModel().getSelectedItem();
                    if (seleccionada != null) {
                        String insertCategoria = "INSERT INTO CATEGORIA_OBJETO (OBJETO_ID, CATEGORIA_ID) VALUES (?, ?)";
                        try (PreparedStatement catStmt = conn.prepareStatement(insertCategoria)) {
                            catStmt.setInt(1, objetoId);
                            catStmt.setInt(2, seleccionada.getId());
                            catStmt.executeUpdate();
                        }
                    }
                }
                mostrarAlerta("Éxito", "Objeto registrado correctamente.", Alert.AlertType.INFORMATION);
                cerrarVentana();
            } else {
                mostrarAlerta("Error", "No se pudo registrar el objeto.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al guardar en la base de datos.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void cancelarRegistro(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void cargarEdificios() {
        ObservableList<Edificio> lista = FXCollections.observableArrayList();
        try (Connection conn = ConexionOracle.getConnection()) {
            String sql = "SELECT ID, NOMBRE FROM EDIFICIOS";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Edificio(rs.getInt("ID"), rs.getString("NOMBRE")));
            }
            cbEdificio.setItems(lista);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarEstados() {
        ObservableList<Estado> lista = FXCollections.observableArrayList();
        try (Connection conn = ConexionOracle.getConnection()) {
            String sql = "SELECT ID, NOMBRE FROM ESTADOS";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Estado(rs.getInt("ID"), rs.getString("NOMBRE")));
            }
            cbEstado.setItems(lista);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarCategorias() {
        ObservableList<Categoria> lista = FXCollections.observableArrayList();
        String sql = "SELECT ID, NOMBRE FROM CATEGORIAS ORDER BY NOMBRE";

        try (Connection conn = ConexionOracle.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Categoria(rs.getInt("ID"), rs.getString("NOMBRE")));
            }
            comboCategoria.setItems(lista);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
