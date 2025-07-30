package com.utez.objetosperdidos.model.dao;

import com.utez.objetosperdidos.model.User;
import com.utez.objetosperdidos.util.ConexionOracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public User obtenerUsuarioPorId(int id) {
        String sql = "SELECT u.ID, u.NOMBRE, u.CORREO, u.CONTRASENA, " +
                 "a.TELEFONO, a.MATRICULA, u.ROL_ID, u.APELLIDOPATERNO, u.APELLIDOMATERNO " +
                 "FROM USUARIOS u " +
                 "LEFT JOIN ALUMNOS a ON u.ID = a.USUARIO_ID " +
                 "WHERE u.ID = ?";

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

public boolean updateUsuario(int id, String nombre, String apellidoPaterno, String apellidoMaterno, String email, String password, String telefono, String matricula) {
    String sqlUsuarios = "UPDATE USUARIOS SET NOMBRE = ?, APELLIDOPATERNO = ?, APELLIDOMATERNO = ?, CORREO = ?, CONTRASENA = ? WHERE ID = ?";
    String sqlAlumnos = "UPDATE ALUMNOS SET TELEFONO = ?, MATRICULA = ? WHERE USUARIO_ID = ?";

    try (Connection conn = ConexionOracle.getConnection()) {
        conn.setAutoCommit(false); // ⚠️ Para asegurar transacción completa

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

        // Actualizar ALUMNOS
        try (PreparedStatement stmt2 = conn.prepareStatement(sqlAlumnos)) {
            stmt2.setString(1, telefono);
            stmt2.setString(2, matricula);
            stmt2.setInt(3, id);
            stmt2.executeUpdate();
        }

        conn.commit();
        return true;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
        }
    }
}
