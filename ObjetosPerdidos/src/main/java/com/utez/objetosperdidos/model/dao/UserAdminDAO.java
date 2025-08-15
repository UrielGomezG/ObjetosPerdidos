package com.utez.objetosperdidos.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.utez.objetosperdidos.model.User;
import com.utez.objetosperdidos.util.ConexionOracle;

public class UserAdminDAO {

    public User obtenerUsuarioPorId(int id) {
        String sql = "SELECT ID, NOMBRE, CORREO, CONTRASENA, ROL_ID, APELLIDOPATERNO, APELLIDOMATERNO from USUARIOS where ID = ? ";

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
                        rs.getString("APELLIDOMATERNO"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateUsuario(int id, String nombre, String apellidoPaterno, String apellidoMaterno, String email,
            String password) {
        String sqlUsuarios = "UPDATE USUARIOS SET NOMBRE = ?, APELLIDOPATERNO = ?, APELLIDOMATERNO = ?, CORREO = ?, CONTRASENA = ? WHERE ID = ?";

        try (Connection conn = ConexionOracle.getConnection()) {
            conn.setAutoCommit(false);

            // Actualizar USUARIOS
            try (PreparedStatement stmt1 = conn.prepareStatement(sqlUsuarios)) {
                stmt1.setString(1, nombre);
                stmt1.setString(2, apellidoPaterno);
                stmt1.setString(3, apellidoMaterno);
                stmt1.setString(4, email);
                stmt1.setString(5, password);
                stmt1.setInt(6, id);
                stmt1.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
