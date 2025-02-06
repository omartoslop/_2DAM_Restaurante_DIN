module org.example._2dam_restaurante_din {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example._2dam_restaurante_din to javafx.fxml;
    exports org.example._2dam_restaurante_din;
}