package org.example._2dam_restaurante_din;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PedidoDAO {

    public void agregarPedido(Pedido pedido) {
        String sql = "INSERT INTO Pedidos (cliente, fecha_pedido, hora_pedido, total, estado) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pedido.getCliente());
            pstmt.setString(2, pedido.getFechaPedido());
            pstmt.setString(3, pedido.getHoraPedido());
            pstmt.setDouble(4, pedido.getTotal());
            pstmt.setString(5, pedido.getEstado());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizarPedido(Pedido pedido) {

    }
}