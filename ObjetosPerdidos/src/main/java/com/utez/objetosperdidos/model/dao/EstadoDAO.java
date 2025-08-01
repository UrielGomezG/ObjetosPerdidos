package com.utez.objetosperdidos.model.dao;
import com.utez.objetosperdidos.model.Estado;
import com.utez.objetosperdidos.util.ConexionOracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstadoDAO {

    public List<Estado> obtenerTodos() {
        List<Estado> estados = new ArrayList<>();
        String sql = "SELECT ID, NOMBRE FROM ESTADOS ORDER BY NOMBRE";

        try (Connection conn = ConexionOracle.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("ID");
                String nombre = rs.getString("NOMBRE");
                estados.add(new Estado(id, nombre));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return estados;
    }
}

