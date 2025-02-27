package org.example._2dam_restaurante_din;

public class DetallePedido {
    private int id;
    private int idPedido; // O bien, podrías almacenar el objeto Pedido
    private Producto producto;
    private int cantidad;
    private double precio;
    private double subtotal;

    public DetallePedido() {}

    public DetallePedido(int id, int idPedido, Producto producto, int cantidad, double precio, double subtotal) {
        this.id = id;
        this.idPedido = idPedido;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.subtotal = subtotal;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}
