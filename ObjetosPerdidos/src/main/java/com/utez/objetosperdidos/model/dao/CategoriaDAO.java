package com.utez.objetosperdidos.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.utez.objetosperdidos.model.Categoria;
import com.utez.objetosperdidos.util.ConexionOracle;


public class CategoriaDAO {
public List<Categoria> obtenerCategorias() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT ID, NOMBRE FROM CATEGORIAS ORDER BY NOMBRE";
        try (Connection conn = ConexionOracle.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Categoria(rs.getInt("ID"), rs.getString("NOMBRE")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean guardarRelacionObjetoCategoria(int objetoId, int categoriaId) {
        String sql = "INSERT INTO CATEGORIA_OBJETO (OBJETO_ID, CATEGORIA_ID) VALUES (?, ?)";
        try (Connection conn = ConexionOracle.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, objetoId);
            ps.setInt(2, categoriaId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
