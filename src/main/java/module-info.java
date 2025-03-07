module com.example.demo {
    requires javafx.fxml;
    requires java.sql;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires twilio;
    requires firebase.admin;
    requires org.controlsfx.controls;
    requires com.google.auth.oauth2;
    requires com.google.auth;
    requires mysql.connector.java;
    requires java.mail;


    exports com.example.demo.controllers.controllers_taxi;
    opens com.example.demo.controllers.controllers_taxi to javafx.fxml;
    exports com.example.demo.tests;
    opens com.example.demo.tests to javafx.fxml;
}