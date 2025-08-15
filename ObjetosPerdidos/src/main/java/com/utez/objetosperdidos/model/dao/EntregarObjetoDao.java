package com.utez.objetosperdidos.model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntregarObjetoDao {
    private final Connection connection;

    public EntregarObjetoDao(Connection connection) {
        this.connection = connection;
    }

    public long crearEntrega(long objetoId, String fotoCredencialUrl) throws SQLException {
        String sql = "INSERT INTO ENTREGAS_OBJETO (OBJETO_ID, FOTO_CREDENCIAL_URL) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"ID"})) {
            stmt.setLong(1, objetoId);
            stmt.setString(2, fotoCredencialUrl);

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    throw new SQLException("No se pudo obtener el ID generado");
                }
            }
        }
    }

    public List<Long> obtenerEntregasPorObjeto(long objetoId) throws SQLException {
        List<Long> entregas = new ArrayList<>();
        String sql = "SELECT ID FROM ENTREGAS_OBJETO WHERE OBJETO_ID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, objetoId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    entregas.add(rs.getLong("ID"));
                }
            }
        }
        return entregas;
    }

    public Entrega obtenerEntrega(long entregaId) throws SQLException {
        String sql = "SELECT ID, OBJETO_ID, FOTO_CREDENCIAL_URL, FECHA_ENTREGA " +
                "FROM ENTREGAS_OBJETO WHERE ID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, entregaId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Entrega(
                            rs.getLong("ID"),
                            rs.getLong("OBJETO_ID"),
                            rs.getString("FOTO_CREDENCIAL_URL"),
                            rs.getTimestamp("FECHA_ENTREGA")
                    );
                }
                return null;
            }
        }
    }

    public boolean actualizarFotoCredencial(long entregaId, String nuevaUrl) throws SQLException {
        String sql = "UPDATE ENTREGAS_OBJETO SET FOTO_CREDENCIAL_URL = ? WHERE ID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nuevaUrl);
            stmt.setLong(2, entregaId);

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean eliminarEntrega(long entregaId) throws SQLException {
        String sql = "DELETE FROM ENTREGAS_OBJETO WHERE ID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, entregaId);
            return stmt.executeUpdate() > 0;
        }
    }

    public static class Entrega {
        private final long id;
        private final long objetoId;
        private final String fotoCredencialUrl;
        private final Timestamp fechaEntrega;

        public Entrega(long id, long objetoId, String fotoCredencialUrl, Timestamp fechaEntrega) {
            this.id = id;
            this.objetoId = objetoId;
            this.fotoCredencialUrl = fotoCredencialUrl;
            this.fechaEntrega = fechaEntrega;
        }

        public long getId() { return id; }
        public long getObjetoId() { return objetoId; }
        public String getFotoCredencialUrl() { return fotoCredencialUrl; }
        public Timestamp getFechaEntrega() { return fechaEntrega; }

        @Override
        public String toString() {
            return String.format(
                    "Entrega[id=%d, objetoId=%d, fotoUrl=%s, fecha=%s]",
                    id, objetoId, fotoCredencialUrl, fechaEntrega
            );
        }
    }
}
