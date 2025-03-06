module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires qrgen;
    requires itextpdf;
    requires java.desktop;

    requires org.apache.poi.poi;
    requires javafx.media;
    requires javafx.web;
    requires twilio;
    requires org.controlsfx.controls;
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
    exports com.example.demo.Services.Service_user;
    exports com.example.demo.controllers.controllers_users;
    opens com.example.demo.models.models_user to javafx.base;  // Permet l'accès via réflexion
    exports com.example.demo;
    exports com.example.demo.interfaces.interfaces_user;
    opens com.example.demo.enums.enums_user to javafx.base;
    exports com.example.demo.tests;

}


