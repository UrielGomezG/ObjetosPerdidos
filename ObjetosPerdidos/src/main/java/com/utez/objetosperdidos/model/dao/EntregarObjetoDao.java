package com.utez.objetosperdidos.model.dao;

import com.utez.objetosperdidos.model.EntregaObjeto;
import com.utez.objetosperdidos.util.ConexionOracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;

public class EntregarObjetoDao {

    private final String INSERT_SQL = """
        INSERT INTO ENTREGAS_OBJETO (OBJETO_ID, FOTO_CREDENCIAL_URL, FECHA_ENTREGA)
        VALUES (?, ?, ?)
    """;

    public boolean registrarEntrega(EntregaObjeto entrega) {
        try (Connection conn = ConexionOracle.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {

            stmt.setLong(1, entrega.getObjetoId());
            stmt.setString(2, entrega.getFotoCredencialUrl());
            stmt.setDate(3, entrega.getFechaEntrega() != null
                    ? new Date(entrega.getFechaEntrega().getTime())
                    : new Date(System.currentTimeMillis()));

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
