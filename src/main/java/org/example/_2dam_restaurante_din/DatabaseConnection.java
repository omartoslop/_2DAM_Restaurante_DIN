package org.example._2dam_restaurante_din;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/restaurantedb";
    private static final String USER = "root";  // Cambia según configuración
    private static final String PASSWORD = "";  // Cambia según configuración

    static {
        try {
            // Cargar explícitamente el controlador JDBC de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("No se pudo cargar el controlador JDBC de MySQL.");
        }
    }

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("No se pudo conectar a la base de datos.", e);
        }
    }
}