package org.example._2dam_restaurante_din;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public int agregarPedido(Pedido pedido) {
        String sql = "INSERT INTO pedidos (id_cliente, fecha_pedido, hora_pedido, total, estado) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, pedido.getCliente().getId());
            pstmt.setDate(2, Date.valueOf(pedido.getFecha()));
            pstmt.setTime(3, Time.valueOf(pedido.getHora()));
            pstmt.setDouble(4, pedido.getTotal());
            pstmt.setString(5, pedido.getEstado());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int idGenerado = rs.getInt(1);
                pedido.setId(idGenerado);
                return idGenerado;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Pedido> obtenerPedidos() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre as cliente_nombre, c.telefono, c.direccion " +
                "FROM pedidos p INNER JOIN clientes c ON p.id_cliente = c.id_cliente";
        try (Connection conn = DatabaseConnection.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("cliente_nombre"),
                        rs.getString("telefono"),
                        rs.getString("direccion")
                );
                Pedido pedido = new Pedido(
                        rs.getInt("id_pedido"),
                        cliente,
                        rs.getDate("fecha_pedido").toLocalDate(),
                        rs.getTime("hora_pedido").toLocalTime(),
                        rs.getDouble("total"),
                        rs.getString("estado")
                );
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pedidos;
    }

    public void eliminarPedido(int idPedido) {
        String sql = "DELETE FROM pedidos WHERE id_pedido = ?";
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPedido);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizarPedido(Pedido pedido) {
        String sql = "UPDATE pedidos SET id_cliente = ?, fecha_pedido = ?, hora_pedido = ?, total = ?, estado = ? WHERE id_pedido = ?";
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, pedido.getCliente().getId());
            pstmt.setDate(2, Date.valueOf(pedido.getFecha()));
            pstmt.setTime(3, Time.valueOf(pedido.getHora()));
            pstmt.setDouble(4, pedido.getTotal());
            pstmt.setString(5, pedido.getEstado());
            pstmt.setInt(6, pedido.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean agregarDetalle(DetallePedido detalle) {
        String sql = "INSERT INTO detalle_pedidos (id_pedido, id_producto, cantidad, precio, subtotal) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, detalle.getIdPedido());
            pstmt.setInt(2, detalle.getProducto().getId());
            pstmt.setInt(3, detalle.getCantidad());
            pstmt.setDouble(4, detalle.getPrecio());
            pstmt.setDouble(5, detalle.getSubtotal());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<DetallePedido> obtenerDetallesPorPedido(int idPedido) {
        List<DetallePedido> detalles = new ArrayList<>();
        String sql = "SELECT dp.*, p.nombre as producto_nombre, p.categoria, p.precio as precio_producto " +
                "FROM detalle_pedidos dp INNER JOIN productos p ON dp.id_producto = p.id_producto " +
                "WHERE dp.id_pedido = ?";
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPedido);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Producto producto = new Producto(
                        rs.getInt("id_producto"),
                        rs.getString("producto_nombre"),
                        rs.getString("categoria"),
                        rs.getDouble("precio_producto"),
                        true
                );
                DetallePedido detalle = new DetallePedido(
                        rs.getInt("id_detalle"),
                        idPedido,
                        producto,
                        rs.getInt("cantidad"),
                        rs.getDouble("precio"),
                        rs.getDouble("subtotal")
                );
                detalles.add(detalle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return detalles;
    }
}
