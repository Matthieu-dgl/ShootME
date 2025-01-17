module com.matthieudeglon.shootme {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires java.sql;

    opens com.matthieudeglon.shootme to javafx.fxml;
    exports com.matthieudeglon.shootme;
    exports com.matthieudeglon.shootme.Views;
}