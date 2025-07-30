package com.utez.objetosperdidos.model.dao;

import com.utez.objetosperdidos.model.User;
import com.utez.objetosperdidos.util.ConexionOracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public User obtenerUsuarioPorId(int id) {
        String sql = "SELECT * FROM USUARIOS WHERE ID = ?";
        try (Connection conn = ConexionOracle.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                    rs.getInt("ID"),
                    rs.getString("NOMBRE"),
                    rs.getString("CORREO"),
                    rs.getString("CONTRASENA"),
                    rs.getString("TELEFONO"),
                    rs.getString("MATRICULA"),
                    rs.getInt("ROL_ID"),
                    rs.getString("APELLIDOPATERNO"),
                    rs.getString("APELLIDOMATERNO")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
public boolean updateUsuario(int id, String nombre, String apellidoPaterno, String apellidoMaterno, String email, String password, String telefono) {
    String sql = "UPDATE USUARIOS SET NOMBRE = ?, APELLIDOPATERNO = ?, APELLIDOMATERNO = ?, CORREO = ?, CONTRASENA = ?, TELEFONO = ? WHERE ID = ?";

    try (Connection conn = ConexionOracle.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, nombre);
        stmt.setString(2, apellidoPaterno);
        stmt.setString(3, apellidoMaterno);
        stmt.setString(4, email);
        stmt.setString(5, password);
        stmt.setString(6, telefono);
        stmt.setInt(7, id);

        int filas = stmt.executeUpdate();
        return filas > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
    }
}