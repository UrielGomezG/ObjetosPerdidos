module com.utez.objetosperdidos {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.utez.objetosperdidos to javafx.fxml;
    exports com.utez.objetosperdidos;
}