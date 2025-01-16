module com.matthieudeglon.shooter2d {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens com.matthieudeglon.shooter2d to javafx.fxml;
    exports com.matthieudeglon.shooter2d;
    exports com.matthieudeglon.shooter2d.Views;
}