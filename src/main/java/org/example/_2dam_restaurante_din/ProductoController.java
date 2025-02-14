package org.example._2dam_restaurante_din;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class ProductoController {
    @FXML private TableView<Producto> tableViewProductos;
    @FXML private TableColumn<Producto, Integer> colId;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TableColumn<Producto, Double> colPrecio;
    @FXML private TableColumn<Producto, Boolean> colDisponibilidad;
    @FXML private TextField txtNombre;
    @FXML private TextField txtCategoria;
    @FXML private TextField txtPrecio;
    @FXML private CheckBox chkDisponible;
    @FXML private TextField txtBuscar;

    private Connection connection;

    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colDisponibilidad.setCellValueFactory(new PropertyValueFactory<>("disponibilidad"));

        connection = DatabaseConnection.conectar();
        if (connection != null) {
            cargarProductos();
        } else {
            mostrarAlerta("Error de conexión", "No se pudo conectar a la base de datos.");
        }
    }

    private void cargarProductos() {
        ObservableList<Producto> productos = FXCollections.observableArrayList();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM productos")) {
            while (resultSet.next()) {
                productos.add(new Producto(
                        resultSet.getInt("id_producto"),
                        resultSet.getString("nombre"),
                        resultSet.getString("categoria"),
                        resultSet.getDouble("precio"),
                        resultSet.getBoolean("disponibilidad")
                ));
            }
            tableViewProductos.setItems(productos);
        } catch (SQLException e) {
            mostrarAlerta("Error de carga", "No se pudieron cargar los productos.");
            e.printStackTrace();
        }
    }

    @FXML
    private void guardarProducto() {
        String nombre = txtNombre.getText();
        String categoria = txtCategoria.getText();
        double precio;
        try {
            precio = Double.parseDouble(txtPrecio.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de formato", "El precio debe ser un número válido.");
            return;
        }
        boolean disponibilidad = chkDisponible.isSelected();

        if (nombre.isEmpty() || categoria.isEmpty()) {
            mostrarAlerta("Campos vacíos", "Por favor, complete todos los campos.");
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO productos (nombre, categoria, precio, disponibilidad) VALUES (?, ?, ?, ?)"
            );
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, categoria);
            preparedStatement.setDouble(3, precio);
            preparedStatement.setBoolean(4, disponibilidad);
            preparedStatement.executeUpdate();
            cargarProductos();
            limpiarCampos();
        } catch (SQLException e) {
            mostrarAlerta("Error de guardado", "No se pudo guardar el producto.");
            e.printStackTrace();
        }
    }

    @FXML
    private void eliminarProducto() {
        Producto productoSeleccionado = tableViewProductos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado == null) {
            mostrarAlerta("Ningún producto seleccionado", "Seleccione un producto de la tabla.");
            return;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM productos WHERE id_producto = ?"
            );
            preparedStatement.setInt(1, productoSeleccionado.getId());
            preparedStatement.executeUpdate();
            cargarProductos();
        } catch (SQLException e) {
            mostrarAlerta("Error de eliminación", "No se pudo eliminar el producto.");
            e.printStackTrace();
        }
    }

    @FXML
    private void editarProducto() {
        Producto productoSeleccionado = tableViewProductos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado == null) {
            mostrarAlerta("Ningún producto seleccionado", "Seleccione un producto de la tabla.");
            return;
        }

        String nombre = txtNombre.getText();
        String categoria = txtCategoria.getText();
        double precio;
        try {
            precio = Double.parseDouble(txtPrecio.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de formato", "El precio debe ser un número válido.");
            return;
        }
        boolean disponibilidad = chkDisponible.isSelected();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE productos SET nombre = ?, categoria = ?, precio = ?, disponibilidad = ? WHERE id_producto = ?"
            );
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, categoria);
            preparedStatement.setDouble(3, precio);
            preparedStatement.setBoolean(4, disponibilidad);
            preparedStatement.setInt(5, productoSeleccionado.getId());
            preparedStatement.executeUpdate();
            cargarProductos();
            limpiarCampos();
        } catch (SQLException e) {
            mostrarAlerta("Error de edición", "No se pudo editar el producto.");
            e.printStackTrace();
        }
    }

    @FXML
    private void buscarProducto() {
        String buscar = txtBuscar.getText();
        ObservableList<Producto> productos = FXCollections.observableArrayList();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM productos WHERE nombre LIKE ?"
            );
            preparedStatement.setString(1, "%" + buscar + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                productos.add(new Producto(
                        resultSet.getInt("id_producto"),
                        resultSet.getString("nombre"),
                        resultSet.getString("categoria"),
                        resultSet.getDouble("precio"),
                        resultSet.getBoolean("disponibilidad")
                ));
            }
            tableViewProductos.setItems(productos);
        } catch (SQLException e) {
            mostrarAlerta("Error de búsqueda", "No se pudo realizar la búsqueda.");
            e.printStackTrace();
        }
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtCategoria.clear();
        txtPrecio.clear();
        chkDisponible.setSelected(false);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
