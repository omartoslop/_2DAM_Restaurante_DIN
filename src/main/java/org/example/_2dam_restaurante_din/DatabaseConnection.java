package org.example._2dam_restaurante_din;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/RestauranteDB";
    private static final String USER = "root";  // Cambia según configuración
    private static final String PASSWORD = "";  // Cambia según configuración

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

