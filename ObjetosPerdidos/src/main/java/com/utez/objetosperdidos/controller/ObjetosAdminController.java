package com.utez.objetosperdidos.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.utez.objetosperdidos.model.Categoria;
import com.utez.objetosperdidos.model.ObjetoPerdido;
import com.utez.objetosperdidos.model.dao.ObjetoDAO;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;


public class ObjetosAdminController {

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
            System.out.println("❌ Error: contenedorTarjetas no está vinculado. Revisa el fx:id en el FXML.");
        } else {
            System.out.println("✅ Inicializando vista de objetos perdidos...");
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
            VBox tarjeta = crearTarjetaAdmin(obj);
            contenedorTarjetas.getChildren().add(tarjeta);
        }
    }

    private void cargarObjetos() {
        contenedorTarjetas.getChildren().clear();
        List<ObjetoPerdido> objetos = dao.obtenerObjetosConInfo(0, 100);

        for (ObjetoPerdido obj : objetos) {
            VBox tarjeta = crearTarjetaAdmin(obj);
            contenedorTarjetas.getChildren().add(tarjeta);
        }
    }

    private VBox crearTarjetaAdmin(ObjetoPerdido obj) {
        VBox tarjeta = new VBox(10);
        tarjeta.getStyleClass().add("card");
        tarjeta.setPrefWidth(270);

        ImageView imagenObjeto = new ImageView();
        imagenObjeto.setFitWidth(80);
        imagenObjeto.setFitHeight(80);
        imagenObjeto.setPreserveRatio(true);
        imagenObjeto.setSmooth(true);

        if (obj.getFotoUrl() != null && !obj.getFotoUrl().trim().isEmpty()) {
            Path rutaImagen = Paths.get(System.getProperty("user.home"), "Downloads", obj.getFotoUrl().trim());
            if (Files.exists(rutaImagen)) {
                imagenObjeto.setImage(new Image(rutaImagen.toUri().toString()));
            } else {
                System.out.println("Imagen no encontrada: " + rutaImagen.toAbsolutePath());
            }
        }

        Label nombreLabel = new Label("📦 " + obj.getNombreObjeto());
        nombreLabel.getStyleClass().add("card-title");

        Label ubicacionLabel = new Label("📍 " + obj.getEdificio() + " - Aula " + obj.getAula());
        ubicacionLabel.getStyleClass().add("card-subtitle");

        Label estadoLabel = new Label("🔖 Estado: " + obj.getEstado());
        estadoLabel.getStyleClass().add("card-subtitle");

        VBox infoTexto = new VBox(4);
        infoTexto.getStyleClass().add("card-info-text");
        infoTexto.getChildren().addAll(nombreLabel, ubicacionLabel, estadoLabel);

        HBox encabezado = new HBox(12);
        encabezado.getStyleClass().add("card-header");
        encabezado.setAlignment(Pos.CENTER_LEFT);
        encabezado.getChildren().addAll(imagenObjeto, infoTexto);

        Label lblDescripcion = new Label(obj.getDescripcion());
        lblDescripcion.getStyleClass().add("card-description");

        Button btnEditar = new Button("Editar");
        btnEditar.setGraphic(new FontIcon("fa-pencil"));
        btnEditar.getStyleClass().add("card-button");

        Button btnEliminar = new Button("Eliminar");
        btnEliminar.setGraphic(new FontIcon("fa-trash"));
        btnEliminar.getStyleClass().addAll("card-button", "delete-button");

        Button btnEnviarBodega = new Button("Enviar a Bodega");
        btnEnviarBodega.setGraphic(new FontIcon("fa-archive"));
        btnEnviarBodega.getStyleClass().add("card-button");

        Button btnEntregar = new Button("Marcar como entregado");
        btnEntregar.setGraphic(new FontIcon("fa-check-circle"));
        btnEntregar.getStyleClass().addAll("card-button", "primary-button");

        // Acciones
        btnEditar.setOnAction(e -> abrirEdicion(obj));
        btnEliminar.setOnAction(e -> eliminarObjeto(obj));
        btnEnviarBodega.setOnAction(e -> enviarABodega(obj));
        btnEntregar.setOnAction(e -> abrirEntrega(obj));

        VBox botones = new VBox(5);
        botones.getStyleClass().add("card-button-container");
        botones.getChildren().addAll(btnEntregar, btnEditar, btnEnviarBodega, btnEliminar);

        tarjeta.getChildren().addAll(encabezado, lblDescripcion, botones);

        return tarjeta;
    }


    private void abrirEdicion(ObjetoPerdido obj) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/utez/objetosperdidos/view/Editar_Objeto.fxml"));
            Parent root = loader.load();

            EditarObjetoController controller = loader.getController();
            controller.setObjetoEditando(obj);

            Stage modal = new Stage();
            modal.setTitle("Editar Objeto");
            modal.setScene(new Scene(root));
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.showAndWait();

            cargarObjetos();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   private void eliminarObjeto(ObjetoPerdido obj) {
    
    if ("Archivado".equalsIgnoreCase(obj.getEstado())) {
        mostrarError("No se puede eliminar un objeto archivado.");
        return;
    }

    boolean confirmado = mostrarConfirmacion("¿Eliminar el objeto '" + obj.getNombreObjeto() + "'?");
    
    if (confirmado) {
        // Primero elimina relaciones con categorías
        boolean relacionesEliminadas = dao.eliminarRelacionesCategoriaObjeto(obj.getId());

        if (!relacionesEliminadas) {
            mostrarError("No se pudieron eliminar las relaciones del objeto con sus categorías.");
            return;
        }

        // Ahora sí se puede eliminar el objeto
        boolean objetoEliminado = dao.eliminarObjeto(obj.getId());
        if (objetoEliminado) {
            mostrarInfo("Objeto eliminado correctamente.");
            cargarObjetos(); // refresca la vista
        } else {
            mostrarError("No se pudo eliminar el objeto.");
        }
    }
}


    private void abrirEntrega(ObjetoPerdido obj) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/utez/objetosperdidos/view/Entregar_Objeto.fxml"));
            Parent root = loader.load();

            EntregarObjetoController controller = loader.getController();
            controller.setObjeto(obj);

            Stage modal = new Stage();
            modal.setTitle("Registrar Entrega");
            modal.setScene(new Scene(root));
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.showAndWait();

            cargarObjetos();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void enviarABodega(ObjetoPerdido obj) {
        boolean confirmado = mostrarConfirmacion("¿Enviar el objeto '" + obj.getNombreObjeto() + "' a bodega?");
        if (confirmado) {
            // Enviar objeto a bodega usando el método correcto del DAO
            if (dao.enviarObjetoABodega(obj.getId())) {
                mostrarInfo("Objeto enviado a bodega correctamente.");
                cargarObjetos();
            } else {
                mostrarError("Error al enviar el objeto a bodega.");
            }
        }
    }

    private boolean mostrarConfirmacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        return alert.showAndWait().filter(btn -> btn == ButtonType.OK).isPresent();
    }

    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
