package org.example._2dam_restaurante_din;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HelloController {
    @FXML
    private Label welcomeText;
    @FXML
    Button clientesButton;
    @FXML
    Button productosButton;
    @FXML
    Button pedidosButton;
    @FXML
    public void clientesButtonOnAction(ActionEvent actionEvent) {
        navigateTo("clientes-view.fxml" , actionEvent);
    }
    @FXML
    public void buttonProductosOnAction(ActionEvent actionEvent) {
        navigateTo("productos-view.fxml" , actionEvent);
    }
    @FXML
    public void buttonPedidosOnAction(ActionEvent actionEvent) {
        navigateTo("pedido-view.fxml" , actionEvent);
    }

    @FXML
    private void navigateTo(String fxmlFile, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource(fxmlFile));
            Stage stage = (Stage)this.pedidosButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}