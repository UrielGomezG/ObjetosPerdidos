package com.utez.objetosperdidos.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.utez.objetosperdidos.model.Categoria;
import com.utez.objetosperdidos.model.ObjetoPerdido;
import com.utez.objetosperdidos.util.ConexionOracle;

public class ObjetoDAO {

    public List<ObjetoPerdido> obtenerObjetosConInfo(int offset, int limit) {
        List<ObjetoPerdido> lista = new ArrayList<>();

        String query = """
                SELECT o.ID, o.NOMBRE_EN_OBJETO, o.DESCRIPCION, o.FOTO_URL, o.AULA,
                o.MARCA, o.MODELO, o.NO_SERIAL,
                e.NOMBRE AS edificio, s.NOMBRE AS estado,
                c.ID AS CATEGORIA_ID, c.NOMBRE AS CATEGORIA_NOMBRE
                FROM OBJETOS_PERDIDOS o
                JOIN EDIFICIOS e ON o.EDIFICIO_ID = e.ID
                JOIN ESTADOS s ON o.ESTADO_ID = s.ID
                LEFT JOIN CATEGORIA_OBJETO co ON o.ID = co.OBJETO_ID
                LEFT JOIN CATEGORIAS c ON co.CATEGORIA_ID = c.ID
                ORDER BY o.FECHA_REPORTE DESC
                """;

        try (Connection conn = ConexionOracle.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ObjetoPerdido obj = new ObjetoPerdido(
                        rs.getInt("ID"),
                        rs.getString("NOMBRE_EN_OBJETO"),
                        rs.getString("DESCRIPCION"),
                        rs.getString("FOTO_URL"),
                        rs.getString("AULA"),
                        rs.getString("edificio"),
                        rs.getString("estado"),
                        rs.getString("MARCA"),
                        rs.getString("MODELO"),
                        rs.getString("NO_SERIAL"));
                String categoriaNombre = rs.getString("CATEGORIA_NOMBRE");
                int categoriaId = rs.getInt("CATEGORIA_ID");
                if (categoriaNombre != null) {
                    obj.setCategoria(new Categoria(categoriaId, categoriaNombre));
                }

                lista.add(obj);
            }

            System.out.println("Objetos encontrados en DAO: " + lista.size());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Categoria> obtenerCategorias() {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT ID, NOMBRE FROM CATEGORIAS ORDER BY NOMBRE";

        try (Connection conn = ConexionOracle.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                categorias.add(new Categoria(rs.getInt("ID"), rs.getString("NOMBRE")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categorias;
    }

   public boolean eliminarRelacionesCategoriaObjeto(int objetoId) {
    
    String sql = "DELETE FROM CATEGORIA_OBJETO WHERE OBJETO_ID = ?";
    try (Connection conn = ConexionOracle.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, objetoId);
        ps.executeUpdate();
        return true;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
        }
    }



    public boolean eliminarObjeto(int idObjeto) {
        String sql = "DELETE FROM OBJETOS_PERDIDOS WHERE ID = ?";
        try (Connection conn = ConexionOracle.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idObjeto);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean marcarComoEntregadoPorAlumno(int idObjeto, int idAlumno) {
        String sql = """
                    UPDATE OBJETOS_PERDIDOS
                    SET ESTADO_ID = 21
                    WHERE ID = ?
                """;

        try (Connection conn = ConexionOracle.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idObjeto);
            boolean resultado = stmt.executeUpdate() > 0;
            
            // Aquí podrías agregar lógica adicional para registrar la entrega
            // Por ejemplo, insertar en una tabla de entregas con fecha, objeto_id, alumno_id, etc.
            
            return resultado;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarObjeto(ObjetoPerdido obj) {
        String sql = """
                    UPDATE OBJETOS_PERDIDOS
                    SET NOMBRE_EN_OBJETO = ?,
                        DESCRIPCION = ?,
                        FOTO_URL = ?,
                        AULA = ?,
                        MARCA = ?,
                        MODELO = ?,
                        NO_SERIAL = ?,
                        EDIFICIO_ID = ?,
                        ESTADO_ID = ?
                    WHERE ID = ?
                """;

        try (Connection conn = ConexionOracle.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, obj.getNombreObjeto());
            stmt.setString(2, obj.getDescripcion());
            stmt.setString(3, obj.getFotoUrl());
            stmt.setString(4, obj.getAula());
            stmt.setString(5, obj.getMarca());
            stmt.setString(6, obj.getModelo());
            stmt.setString(7, obj.getNo_serial());
            stmt.setInt(8, obj.getEdificioId());
            stmt.setInt(9, obj.getEstadoId());
            stmt.setInt(10, obj.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cambiarEstadoObjeto(int idObjeto, int nuevoEstadoId) {
        String sql = "UPDATE OBJETOS_PERDIDOS SET ESTADO_ID = ? WHERE ID = ?";
        try (Connection conn = ConexionOracle.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, nuevoEstadoId);
            stmt.setInt(2, idObjeto);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean enviarObjetoABodega(int idObjeto) {
        Connection conn = null;
        try {
            conn = ConexionOracle.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            // 1. Insertar registro en OBJETOS_ALMACEN
            String sqlInsert = "INSERT INTO OBJETOS_ALMACEN (FECHA_ENVIO, OBSERVACIONES, OBJETO_PERDIDO_ID, ESTADO_ID) VALUES (SYSDATE, ?, ?, ?)";
            try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {
                stmtInsert.setString(1, "Enviado a bodega por administrador");
                stmtInsert.setInt(2, idObjeto);
                stmtInsert.setInt(3, 3); // Estado "Disponible para Reclamar"
                stmtInsert.executeUpdate();
            }

            // 2. Actualizar estado en OBJETOS_PERDIDOS
            String sqlUpdate = "UPDATE OBJETOS_PERDIDOS SET ESTADO_ID = ? WHERE ID = ?";
            try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
                stmtUpdate.setInt(1, 3); // Estado "Disponible para Reclamar"
                stmtUpdate.setInt(2, idObjeto);
                stmtUpdate.executeUpdate();
            }

            conn.commit(); // Confirmar transacción
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Revertir en caso de error
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        ObjetoDAO dao = new ObjetoDAO();
        List<ObjetoPerdido> objetos = dao.obtenerObjetosConInfo(0, 6);

        System.out.println("Total de objetos encontrados: " + objetos.size());
        for (ObjetoPerdido obj : objetos) {
            System.out.println("ID: " + obj.getId());
            System.out.println("Nombre: " + obj.getNombreObjeto());
            System.out.println("Marca: " + obj.getMarca());
            System.out.println("Modelo: " + obj.getModelo());
            System.out.println("No. Serie: " + obj.getNo_serial());
            System.out.println("Descripción: " + obj.getDescripcion());
            System.out.println("Edificio: " + obj.getEdificio());
            System.out.println("Estado: " + obj.getEstado());
            System.out.println("-----------------------------");
        }
    }
}
