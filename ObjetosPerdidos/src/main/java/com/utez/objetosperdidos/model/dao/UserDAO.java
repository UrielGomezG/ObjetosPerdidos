package com.utez.objetosperdidos.model.dao;

import com.utez.objetosperdidos.model.ObjetoPerdido;
import com.utez.objetosperdidos.model.User;
import com.utez.objetosperdidos.util.ConexionOracle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class UserDAO {

    public User obtenerUsuarioPorId(int id) {
    String sql = "SELECT * FROM USUARIOS WHERE ID = ?";
    try (Connection conn = ConexionOracle.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return new User(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("correo"),
                rs.getString("contrasena"),
                rs.getString("telefono"),
                rs.getString("matricula"),
                rs.getInt("rol"),
                rs.getString("apellidoPaterno"),
                rs.getString("apellidoMaterno")
            );
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

     
public boolean updateUsuario(int id, String nombre, String apellidoPaterno, String apellidoMaterno, String email, String password) {
        String sql = "UPDATE USUARIOS SET NOMBRE = ?, APELLIDOPATERNO = ?, APELLIDOMATERNO = ?, CORREO = ?, CONTRASENA = ? WHERE ID = ?";

        try (Connection conn = ConexionOracle.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setString(2, apellidoPaterno);
            stmt.setString(3, apellidoMaterno);
            stmt.setString(4, email);
            stmt.setString(5, password);
            stmt.setInt(6, id);

            int filas = stmt.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}