module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.persistence;
    requires static lombok;
    requires java.sql;


    exports com.example.demo.tests;
    opens com.example.demo.tests to javafx.fxml;
    exports com.example.demo.controllers.controllers_TP;
    opens com.example.demo.controllers.controllers_TP to javafx.fxml;
}