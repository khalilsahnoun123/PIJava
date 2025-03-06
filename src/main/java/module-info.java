module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires qrgen;
    requires itextpdf;
    requires java.net.http;
    requires  json.simple;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;

    requires org.apache.poi.poi;
    requires javafx.media;
    requires javafx.web;
    requires twilio;
    requires org.controlsfx.controls;

    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    requires com.almasb.fxgl.all;
    requires static lombok;
    requires java.persistence;
    requires org.apache.poi.ooxml;

    exports com.example.demo.controllers.controllers_cov;
    opens com.example.demo.controllers.controllers_cov to javafx.fxml;

    exports com.example.demo.controllers.controllers_TP;
    opens com.example.demo.controllers.controllers_TP to javafx.fxml;

    exports com.example.demo.models.models_velo to com.fasterxml.jackson.databind;
    opens com.example.demo.models.models_velo to com.fasterxml.jackson.databind;

    opens com.example.demo.controllers.controllers_velo to javafx.fxml;
    exports com.example.demo.controllers.controllers_velo;
    requires stripe.java;
    requires fontawesomefx;
    requires jakarta.mail;
    requires jbcrypt;
    requires opencv;
    requires kaptcha;
    requires nanohttpd;
    requires org.eclipse.jetty.server;
    requires javax.servlet.api;
    requires org.eclipse.jetty.servlet;
    requires scribejava.core;
    requires scribejava.apis;
    requires spark.core;
    requires org.json;

    opens com.example.demo.controllers.controllers_users to javafx.fxml;
    exports com.example.demo.controllers.controllers_users;
    opens com.example.demo.models.models_user to javafx.base;  // Permet l'accès via réflexion
    exports  com.example.demo.models.models_user;
    exports com.example.demo.interfaces.interfaces_user;
    opens com.example.demo.enums.enums_user to javafx.base;

    exports com.example.demo.tests;
    opens com.example.demo.tests to javafx.fxml;
}