module com.example.serwer2023 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires java.sql;

    opens com.example.serwer2023 to javafx.fxml;
    exports com.example.serwer2023;
}