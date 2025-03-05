module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires itextpdf;
    requires java.desktop;
    requires java.net.http;

    requires com.fasterxml.jackson.databind;

    requires javafx.web;


    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    requires com.almasb.fxgl.all;
    requires static lombok;
    requires java.persistence;

    exports com.example.demo.controllers.controllers_cov;
    opens com.example.demo.controllers.controllers_cov to javafx.fxml;

    exports com.example.demo.controllers.controllers_TP;
    opens com.example.demo.controllers.controllers_TP to javafx.fxml;

    exports com.example.demo.models.models_velo to com.fasterxml.jackson.databind;
    opens com.example.demo.models.models_velo to com.fasterxml.jackson.databind;

    opens com.example.demo.controllers.controllers_velo to javafx.fxml;
    exports com.example.demo.controllers.controllers_velo;

    exports com.example.demo.tests;
    opens com.example.demo.tests to javafx.fxml;
}