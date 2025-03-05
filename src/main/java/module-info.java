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


    exports controllers.controllers_taxi;
    opens controllers.controllers_taxi to javafx.fxml;
    exports tests;
    opens tests to javafx.fxml;
}