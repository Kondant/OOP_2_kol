module com.example.client2023 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.client2023 to javafx.fxml;
    exports com.example.client2023;
}