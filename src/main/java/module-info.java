module com.saveit {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;

    opens com.saveit.controller to javafx.fxml;
    opens com.saveit.model to javafx.base;
    exports com.saveit;
}