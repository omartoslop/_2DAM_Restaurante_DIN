package org.example._2dam_restaurante_din;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class PedidoController {
    @FXML
    private TableView<Pedido> tableViewPedidos;
    @FXML
    private TableColumn<Pedido, Integer> colIdPedido;
    @FXML
    private TableColumn<Pedido, String> colCliente;
    @FXML
    private TableColumn<Pedido, String> colFechaPedido;
    @FXML
    private TableColumn<Pedido, String> colHoraPedido;
    @FXML
    private TableColumn<Pedido, Double> colTotal;
    @FXML
    private TableColumn<Pedido, String> colEstado;
    @FXML
    private TextField txtClienteId; // ID del Cliente
    @FXML
    private TextField txtFechaPedido;
    @FXML
    private TextField txtHoraPedido;
    @FXML
    private TextField txtTotal;
    @FXML
    private TextField txtEstado;
    @FXML
    private Button backButton;

    private ObservableList<Pedido> pedidos;

    @FXML
    public void initialize() {
        pedidos = FXCollections.observableArrayList();
        cargarPedidos();
    }

    private void cargarPedidos() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurante", "user", "password");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Pedidos");

            while (resultSet.next()) {
                Pedido pedido = new Pedido(
                        resultSet.getInt("id_pedido"),
                        resultSet.getInt("id_cliente"),
                        resultSet.getString("fecha_pedido"),
                        resultSet.getString("hora_pedido"),
                        resultSet.getDouble("total"),
                        resultSet.getString("estado")
                );
                pedidos.add(pedido);
            }

            tableViewPedidos.setItems(pedidos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void crearPedido(ActionEvent event) {
        int idCliente = Integer.parseInt(txtClienteId.getText()); // Cambiado para usar idCliente
        String fechaPedido = txtFechaPedido.getText();
        String horaPedido = txtHoraPedido.getText();
        double total = Double.parseDouble(txtTotal.getText());
        String estado = txtEstado.getText();

        Pedido nuevoPedido = new Pedido(0, idCliente, fechaPedido, horaPedido, total, estado);
        PedidoDAO pedidoDAO = new PedidoDAO();
        pedidoDAO.agregarPedido(nuevoPedido);

        pedidos.add(nuevoPedido); // Agregar el nuevo pedido a la lista
        tableViewPedidos.refresh(); // Refrescar la tabla
    }

    @FXML
    private void volver(ActionEvent event) {
        navigateTo("hello-view.fxml", event);
    }

    @FXML
    private void navigateTo(String fxmlFile, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource(fxmlFile));
            Stage stage = (Stage) this.backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
