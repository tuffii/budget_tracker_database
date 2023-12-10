module com.example.bd_1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires jbcrypt;

    opens bd to javafx.fxml;
    exports bd;
    exports bd.controllers;
    opens bd.controllers to javafx.fxml;
    exports bd.TableModels;
    opens bd.TableModels to javafx.fxml;
}