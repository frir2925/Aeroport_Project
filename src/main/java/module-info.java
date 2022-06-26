module aeroport {
    requires javafx.controls;
    requires javafx.fxml;
    requires  javafx.graphics;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens aeroport to javafx.fxml;
    exports aeroport;
}