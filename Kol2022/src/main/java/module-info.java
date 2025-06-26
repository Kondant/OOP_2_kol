module com.example.kol2022 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.kol2022 to javafx.fxml;
    exports com.example.kol2022;
}