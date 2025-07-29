package com.utez.objetosperdidos.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionOracle {
    private static final String URL      = "jdbc:oracle:thin:@r7xq6ml0aoop7hf1_high"; //Copiar desde tnsnames y pegarlo despues de thin:
    private static final String USER     = "ADMIN";
    private static final String PASSWORD = "qaJp(E6r&Q2W^lHrE"; // Colocar la password

    // Obtiene una conexión nueva
    public static Connection getConnection() throws SQLException {
        // 1. Apunta al directorio donde descomprimiste el wallet
        System.setProperty("oracle.net.tns_admin", "C:\\Users\\diego\\Documents\\integradora\\Wallet_R7XQ6ML0AOOP7HF1"); //Colocar la ruta de tu Wallet
        // 2. (Opcional) fuerza la validación de nombre de servidor en el certificado
        System.setProperty("oracle.net.ssl_server_dn_match", "true");
        // 3. Obtiene la conexión usando alias, user y pass
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

 public static void main(String[] args) throws SQLException {
      try (Connection conn = getConnection()) {
           System.out.println("¡Conexión exitosa!");
     } catch (SQLException e) {
            e.printStackTrace();
       } 
     }

    //Prueba la conexión de la base de datos
}