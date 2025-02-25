package org.example._2dam_restaurante_din;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class EditarPedidoController {
    @FXML
    private TextField txtCliente;
    @FXML
    private TextField txtFechaPedido;
    @FXML
    private TextField txtHoraPedido;
    @FXML
    private TextField txtTotal;
    @FXML
    private TextField txtEstado;

    private Pedido pedido;

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
        txtCliente.setText(pedido.getCliente());
        txtFechaPedido.setText(pedido.getFechaPedido());
        txtHoraPedido.setText(pedido.getHoraPedido());
        txtTotal.setText(String.valueOf(pedido.getTotal()));
        txtEstado.setText(pedido.getEstado());
    }

    @FXML
    private void guardarCambios(ActionEvent event) {
        pedido.setCliente(txtCliente.getText());
        pedido.setFechaPedido(txtFechaPedido.getText());
        pedido.setHoraPedido(txtHoraPedido.getText());
        pedido.setTotal(Double.parseDouble(txtTotal.getText()));
        pedido.setEstado(txtEstado.getText());

        // Actualizar la base de datos con los cambios
        PedidoDAO pedidoDAO = new PedidoDAO();
        pedidoDAO.actualizarPedido(pedido);

        Stage stage = (Stage) txtCliente.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancelar(ActionEvent event) {
        Stage stage = (Stage) txtCliente.getScene().getWindow();
        stage.close();
    }
}