module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    requires com.almasb.fxgl.all;
    requires static lombok;
    requires java.persistence;
    requires java.sql;

    exports com.example.demo.controllers;
    opens com.example.demo.controllers to javafx.fxml;
    exports com.example.demo.tests;
    opens com.example.demo.tests to javafx.fxml;
}