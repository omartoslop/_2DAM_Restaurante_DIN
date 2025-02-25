package org.example._2dam_restaurante_din;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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
    private TextField txtCliente;
    @FXML
    private TextField txtFechaPedido;
    @FXML
    private TextField txtHoraPedido;
    @FXML
    private TextField txtTotal;
    @FXML
    private TextField txtEstado;

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
            ResultSet resultSet = statement.executeQuery("SELECT * FROM pedidos");

            while (resultSet.next()) {
                Pedido pedido = new Pedido(
                    resultSet.getInt("idPedido"),
                    resultSet.getString("cliente"),
                    resultSet.getString("fechaPedido"),
                    resultSet.getString("horaPedido"),
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
        // Implementación para crear un pedido
    }

    @FXML
    private void modificarPedido(ActionEvent event) {
        Pedido selectedPedido = tableViewPedidos.getSelectionModel().getSelectedItem();
        if (selectedPedido != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/_2dam_restaurante_din/editar-pedido-view.fxml"));
                Parent root = loader.load();
                EditarPedidoController controller = loader.getController();
                controller.setPedido(selectedPedido);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Editar Pedido");
                stage.showAndWait();
                tableViewPedidos.refresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void eliminarPedido(ActionEvent event) {
        // Implementación para eliminar un pedido
    }

    @FXML
    private void buscarPedido(ActionEvent event) {
        // Implementación para buscar un pedido
    }

    @FXML
    private void verDetallePedido(ActionEvent event) {
        // Implementación para ver el detalle de un pedido
    }

    @FXML
    private void volver(ActionEvent event) {
        // Implementación para volver a la ventana principal
    }
}