module tn.gestioncov {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires itextpdf;


    exports controllers.controllers_cov;
    opens controllers.controllers_cov to javafx.fxml;


    exports tests;
    opens tests to javafx.fxml;
}