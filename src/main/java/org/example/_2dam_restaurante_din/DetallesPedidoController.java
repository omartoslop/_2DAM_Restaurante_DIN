package org.example._2dam_restaurante_din;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class DetallesPedidoController {
    @FXML
    private TableView<DetallePedido> tableViewDetallePedidos;
    @FXML
    private TableColumn<DetallePedido, Integer> colIdDetallePedido;
    @FXML
    private TableColumn<DetallePedido, Integer> colIdPedido;
    @FXML
    private TableColumn<DetallePedido, Integer> colIdProducto;
    @FXML
    private TableColumn<DetallePedido, Integer> colCantidad;
    @FXML
    private TableColumn<DetallePedido, Double> colPrecio;
    @FXML
    private TableColumn<DetallePedido, Double> colSubtotal;

    @FXML
    private void volver(ActionEvent event) {
        // Implementación para volver a la ventana de pedidos
    }
}