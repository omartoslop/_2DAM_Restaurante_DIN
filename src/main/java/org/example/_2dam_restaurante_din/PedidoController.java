package org.example._2dam_restaurante_din;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class PedidoController {
    @FXML private TableView<Pedido> tableViewPedidos;
    @FXML private TableColumn<Pedido, Integer> colId;
    @FXML private TableColumn<Pedido, String> colCliente;
    @FXML private TableColumn<Pedido, LocalDate> colFecha;
    @FXML private TableColumn<Pedido, LocalTime> colHora;
    @FXML private TableColumn<Pedido, Double> colTotal;
    @FXML private TableColumn<Pedido, String> colEstado;
    @FXML private ComboBox<Cliente> comboClientes;
    @FXML private DatePicker datePickerFecha;
    @FXML private TextField txtHora;
    @FXML private TextField txtTotal;
    @FXML private ComboBox<String> comboEstado;
    @FXML private TextField txtBuscar;
    @FXML private Button backButton;

    private ObservableList<Pedido> pedidos;
    private PedidoDAO pedidoDAO = new PedidoDAO();

    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCliente.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCliente().getNombre()));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        cargarClientesEnCombo();
        comboEstado.setItems(FXCollections.observableArrayList("Pendiente", "En Preparación", "Entregado"));
        pedidos = FXCollections.observableArrayList();
        cargarPedidos();
    }

    private void cargarClientesEnCombo() {
        ClienteDAO clienteDAO = new ClienteDAO();
        List<Cliente> listaClientes = clienteDAO.obtenerClientes();
        comboClientes.setItems(FXCollections.observableArrayList(listaClientes));
    }

    private void cargarPedidos() {
        pedidos.clear();
        List<Pedido> lista = pedidoDAO.obtenerPedidos();
        pedidos.addAll(lista);
        tableViewPedidos.setItems(pedidos);
    }

    @FXML
    private void guardarPedido() {
        if (comboClientes.getValue() == null || datePickerFecha.getValue() == null ||
                txtHora.getText().isEmpty() || txtTotal.getText().isEmpty() || comboEstado.getValue() == null) {
            mostrarAlerta("Campos vacíos", "Complete todos los campos.");
            return;
        }
        try {
            double total = Double.parseDouble(txtTotal.getText());
            Pedido pedido = new Pedido();
            pedido.setCliente(comboClientes.getValue());
            pedido.setFecha(datePickerFecha.getValue());
            pedido.setHora(LocalTime.parse(txtHora.getText()));
            pedido.setTotal(total);
            pedido.setEstado(comboEstado.getValue());
            int idPedido = pedidoDAO.agregarPedido(pedido);
            if (idPedido != -1) {
                mostrarAlerta("Éxito", "Pedido guardado correctamente.");
                cargarPedidos();
            } else {
                mostrarAlerta("Error", "No se pudo guardar el pedido.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de formato", "El total debe ser un número válido.");
        } catch (Exception e) {
            mostrarAlerta("Error", "Hubo un problema al guardar el pedido.");
            e.printStackTrace();
        }
    }

    @FXML
    private void eliminarPedido() {
        Pedido pedidoSeleccionado = tableViewPedidos.getSelectionModel().getSelectedItem();
        if (pedidoSeleccionado == null) {
            mostrarAlerta("Selección requerida", "Seleccione un pedido.");
            return;
        }
        pedidoDAO.eliminarPedido(pedidoSeleccionado.getId());
        cargarPedidos();
    }

    @FXML
    private void editarPedido() {
        Pedido pedidoSeleccionado = tableViewPedidos.getSelectionModel().getSelectedItem();
        if (pedidoSeleccionado == null) {
            mostrarAlerta("Selección requerida", "Seleccione un pedido para editar.");
            return;
        }
        try {
            double total = Double.parseDouble(txtTotal.getText());
            pedidoSeleccionado.setCliente(comboClientes.getValue());
            pedidoSeleccionado.setFecha(datePickerFecha.getValue());
            pedidoSeleccionado.setHora(LocalTime.parse(txtHora.getText()));
            pedidoSeleccionado.setTotal(total);
            pedidoSeleccionado.setEstado(comboEstado.getValue());
            pedidoDAO.actualizarPedido(pedidoSeleccionado);
            mostrarAlerta("Éxito", "Pedido actualizado correctamente.");
            cargarPedidos();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de formato", "El total debe ser un número válido.");
        } catch (Exception e) {
            mostrarAlerta("Error", "Hubo un problema al actualizar el pedido.");
            e.printStackTrace();
        }
    }

    @FXML
    private void buscarPedido() {
        String buscar = txtBuscar.getText().trim();
        if (buscar.isEmpty()) {
            tableViewPedidos.setItems(pedidos);
            return;
        }
        ObservableList<Pedido> pedidosFiltrados = FXCollections.observableArrayList();
        for (Pedido p : pedidos) {
            if (p.getCliente().getNombre().toLowerCase().contains(buscar.toLowerCase()) ||
                    p.getEstado().toLowerCase().contains(buscar.toLowerCase())) {
                pedidosFiltrados.add(p);
            }
        }
        tableViewPedidos.setItems(pedidosFiltrados);
    }

    @FXML
    private void verDetalles(ActionEvent event) {
        Pedido pedidoSeleccionado = tableViewPedidos.getSelectionModel().getSelectedItem();
        if (pedidoSeleccionado == null) {
            mostrarAlerta("Selección requerida", "Seleccione un pedido para ver sus detalles.");
            return;
        }
        DetallePedidoController.setCurrentPedidoId(pedidoSeleccionado.getId());
        navigateTo("detalle-pedido-view.fxml", event);
    }

    @FXML
    private void navigateTo(String fxmlFile, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/_2dam_restaurante_din/" + fxmlFile));
            Stage stage = (Stage) this.backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
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

    public void volver(ActionEvent actionEvent) {
    }
}
