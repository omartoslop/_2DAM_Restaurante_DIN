package org.example._2dam_restaurante_din;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class ClienteController {
    @FXML private TableView<Cliente> tableViewClientes;
    @FXML private TableColumn<Cliente, Integer> colId;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, String> colTelefono;
    @FXML private TableColumn<Cliente, String> colDireccion;
    @FXML private TextField txtNombre;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtBuscar;

    private Connection connection;

    // Método que se ejecuta al cargar la interfaz
    public void initialize() {
        // Configurar las columnas de la TableView
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        // Conectar a la base de datos y cargar los clientes
        connection = DatabaseConnection.conectar();
        if (connection != null) {
            cargarClientes();
        } else {
            mostrarAlerta("Error de conexión", "No se pudo conectar a la base de datos.");
        }
    }

    // Método para cargar los clientes en la TableView
    private void cargarClientes() {
        ObservableList<Cliente> clientes = FXCollections.observableArrayList();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM clientes")) {
            while (resultSet.next()) {
                clientes.add(new Cliente(
                        resultSet.getInt("id_cliente"), // Usar "id_cliente"
                        resultSet.getString("nombre"),
                        resultSet.getString("telefono"),
                        resultSet.getString("direccion")
                ));
            }
            tableViewClientes.setItems(clientes);
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error de carga", "No se pudieron cargar los clientes.");
        }
    }

    // Método para guardar un nuevo cliente
    @FXML
    private void guardarCliente() {
        String nombre = txtNombre.getText();
        String telefono = txtTelefono.getText();
        String direccion = txtDireccion.getText();

        if (nombre.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) {
            mostrarAlerta("Campos vacíos", "Por favor, complete todos los campos.");
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO clientes (nombre, telefono, direccion) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS // Para obtener el ID generado
            );
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, telefono);
            preparedStatement.setString(3, direccion);
            preparedStatement.executeUpdate();

            // Obtener el ID generado automáticamente
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idGenerado = generatedKeys.getInt(1);
                System.out.println("ID generado: " + idGenerado); // Puedes mostrar o usar el ID generado
            }

            cargarClientes(); // Recargar la tabla
            limpiarCampos(); // Limpiar los campos del formulario
        } catch (SQLException e) {
            mostrarAlerta("Error de guardado", "No se pudo guardar el cliente.");
            e.printStackTrace();
        }
    }

    // Método para eliminar un cliente
    @FXML
    private void eliminarCliente() {
        Cliente clienteSeleccionado = tableViewClientes.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado == null) {
            mostrarAlerta("Ningún cliente seleccionado", "Por favor, seleccione un cliente de la tabla.");
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM clientes WHERE id_cliente = ?" // Usar "id_cliente"
            );
            preparedStatement.setInt(1, clienteSeleccionado.getId());
            preparedStatement.executeUpdate();
            cargarClientes(); // Recargar la tabla
        } catch (SQLException e) {
            mostrarAlerta("Error de eliminación", "No se pudo eliminar el cliente.");
            e.printStackTrace();
        }
    }

    // Método para editar un cliente
    @FXML
    private void editarCliente() {
        Cliente clienteSeleccionado = tableViewClientes.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado == null) {
            mostrarAlerta("Ningún cliente seleccionado", "Por favor, seleccione un cliente de la tabla.");
            return;
        }

        String nombre = txtNombre.getText();
        String telefono = txtTelefono.getText();
        String direccion = txtDireccion.getText();

        if (nombre.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) {
            mostrarAlerta("Campos vacíos", "Por favor, complete todos los campos.");
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE clientes SET nombre = ?, telefono = ?, direccion = ? WHERE id_cliente = ?" // Usar "id_cliente"
            );
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, telefono);
            preparedStatement.setString(3, direccion);
            preparedStatement.setInt(4, clienteSeleccionado.getId());
            preparedStatement.executeUpdate();
            cargarClientes(); // Recargar la tabla
            limpiarCampos(); // Limpiar los campos del formulario
        } catch (SQLException e) {
            mostrarAlerta("Error de edición", "No se pudo editar el cliente.");
            e.printStackTrace();
        }
    }

    // Método para buscar clientes por nombre
    @FXML
    private void buscarCliente() {
        String buscar = txtBuscar.getText();
        ObservableList<Cliente> clientes = FXCollections.observableArrayList();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM clientes WHERE nombre LIKE ?"
            );
            preparedStatement.setString(1, "%" + buscar + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                clientes.add(new Cliente(
                        resultSet.getInt("id_cliente"), // Usar "id_cliente"
                        resultSet.getString("nombre"),
                        resultSet.getString("telefono"),
                        resultSet.getString("direccion")
                ));
            }
            tableViewClientes.setItems(clientes);
        } catch (SQLException e) {
            mostrarAlerta("Error de búsqueda", "No se pudo realizar la búsqueda.");
            e.printStackTrace();
        }
    }

    // Método para limpiar los campos del formulario
    private void limpiarCampos() {
        txtNombre.clear();
        txtTelefono.clear();
        txtDireccion.clear();
    }

    // Método para mostrar alertas
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}