package com.utez.objetosperdidos.model.dao;

import com.utez.objetosperdidos.model.EntregaObjeto;
import com.utez.objetosperdidos.util.ConexionOracle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntregarObjetoDao {

    private final String INSERT_SQL = """
    INSERT INTO ENTREGAS_OBJETO 
    (OBJETO_ID, FOTO_CREDENCIAL_URL, FECHA_ENTREGA, NOMBRE_ALUMNO, MATRICULA)
    VALUES (?, ?, ?, ?, ?)
""";

    public boolean registrarEntrega(EntregaObjeto entrega) {
        try (Connection conn = ConexionOracle.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {

            stmt.setLong(1, entrega.getObjetoId());
            stmt.setString(2, entrega.getFotoCredencialUrl());
            stmt.setDate(3, entrega.getFechaEntrega() != null
                    ? new java.sql.Date(entrega.getFechaEntrega().getTime())
                    : new java.sql.Date(System.currentTimeMillis()));
            stmt.setString(4, entrega.getNombreAlumno());
            stmt.setString(5, entrega.getMatricula());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<EntregaObjeto> obtenerEntregaPorObjeto(long objetoId) {
        List<EntregaObjeto> lista = new ArrayList<>();
        String sql = "SELECT OBJETO_ID, FOTO_CREDENCIAL_URL, FECHA_ENTREGA, NOMBRE_ALUMNO, MATRICULA " +
                "FROM ENTREGAS_OBJETO WHERE OBJETO_ID = ?";
        try (Connection conn = ConexionOracle.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, objetoId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new EntregaObjeto(
                        rs.getLong("OBJETO_ID"),
                        rs.getString("FOTO_CREDENCIAL_URL"),
                        rs.getDate("FECHA_ENTREGA"),
                        rs.getString("NOMBRE_ALUMNO"),
                        rs.getString("MATRICULA")
                ));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lista;
    }

}
