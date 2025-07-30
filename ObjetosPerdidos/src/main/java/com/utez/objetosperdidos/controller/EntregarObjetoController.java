package com.utez.objetosperdidos.controller;

import com.utez.objetosperdidos.model.ObjetoPerdido;
import com.utez.objetosperdidos.model.User;
import com.utez.objetosperdidos.model.dao.ObjetoDAO;
import com.utez.objetosperdidos.model.dao.UserDAO;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

    private ObjetoPerdido objeto;
    private final ObjetoDAO dao = new ObjetoDAO();
    private final UserDAO userDAO = new UserDAO();

    public void setObjeto(ObjetoPerdido objeto) {
        this.objeto = objeto;
        lblTitulo.setText(objeto.getNombreObjeto());
        lblMarca.setText(objeto.getMarca());
        lblModelo.setText(objeto.getModelo());
        lblNoSerie.setText(objeto.getNo_serial());
        lblDescripcion.setText(objeto.getDescripcion());
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
        boolean ok = dao.marcarComoEntregadoPorAlumno(objeto.getId(), alumno.getId());

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
}
