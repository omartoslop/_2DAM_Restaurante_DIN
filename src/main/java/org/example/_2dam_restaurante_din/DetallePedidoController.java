package org.example._2dam_restaurante_din;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class DetallePedidoController {
    @FXML private TableView<DetallePedido> tableViewDetalles;
    @FXML private TableColumn<DetallePedido, Integer> colIdDetalle;
    @FXML private TableColumn<DetallePedido, String> colProducto;
    @FXML private TableColumn<DetallePedido, Integer> colCantidad;
    @FXML private TableColumn<DetallePedido, Double> colPrecio;
    @FXML private TableColumn<DetallePedido, Double> colSubtotal;
    @FXML private ComboBox<Producto> comboProductos;
    @FXML private TextField txtCantidad;
    @FXML private Label lblSubtotal;
    @FXML private Button backButton;

    private ObservableList<DetallePedido> detalles;
    private PedidoDAO pedidoDAO = new PedidoDAO();
    private static int currentPedidoId;

    public static void setCurrentPedidoId(int id) {
        currentPedidoId = id;
    }

    public void initialize() {
        colIdDetalle.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProducto.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getProducto().getNombre()));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        detalles = FXCollections.observableArrayList();
        tableViewDetalles.setItems(detalles);
        cargarProductosEnCombo();
        if (currentPedidoId != 0) {
            cargarDetalles();
        }
    }

    public void cargarDetalles() {
        detalles.clear();
        List<DetallePedido> listaDetalles = pedidoDAO.obtenerDetallesPorPedido(currentPedidoId);
        detalles.addAll(listaDetalles);
        tableViewDetalles.setItems(detalles);
    }

    private void cargarProductosEnCombo() {
        ProductoDAO productoDAO = new ProductoDAO();
        List<Producto> listaProductos = productoDAO.obtenerProductos();
        comboProductos.setItems(FXCollections.observableArrayList(listaProductos));
    }

    @FXML
    private void calcularSubtotal() {
        try {
            int cantidad = Integer.parseInt(txtCantidad.getText());
            Producto producto = comboProductos.getValue();
            if (producto != null) {
                double subtotal = cantidad * producto.getPrecio();
                lblSubtotal.setText(String.format("%.2f", subtotal));
            }
        } catch (NumberFormatException e) {
            lblSubtotal.setText("0.00");
        }
    }

    @FXML
    private void agregarDetalle() {
        if (comboProductos.getValue() == null || txtCantidad.getText().isEmpty()) {
            mostrarAlerta("Campos vacíos", "Seleccione un producto y especifique la cantidad.");
            return;
        }
        try {
            int cantidad = Integer.parseInt(txtCantidad.getText());
            Producto producto = comboProductos.getValue();
            double precio = producto.getPrecio();
            double subtotal = cantidad * precio;
            DetallePedido detalle = new DetallePedido(0, currentPedidoId, producto, cantidad, precio, subtotal);
            if (pedidoDAO.agregarDetalle(detalle)) {
                mostrarAlerta("Éxito", "Detalle agregado.");
                cargarDetalles();
            } else {
                mostrarAlerta("Error", "No se pudo agregar el detalle.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de formato", "La cantidad debe ser un número válido.");
        }
    }

    @FXML
    private void volver(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/_2dam_restaurante_din/hello-view.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
