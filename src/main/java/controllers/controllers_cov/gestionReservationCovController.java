package controllers.controllers_cov;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class gestionReservationCovController implements Initializable {

    @FXML
    private Button btnAddReservationCov;

    @FXML
    private Button btnListReservationCov;

    @FXML
    private AnchorPane gestionReservationCovPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void goToPages(ActionEvent event) throws IOException {
        if(event.getSource()== btnAddReservationCov){
            Parent fxml= FXMLLoader.load(getClass().getResource("addReservationCov.fxml"));
            gestionReservationCovPane.getChildren().removeAll();
            gestionReservationCovPane.getChildren().setAll(fxml);
        }else if(event.getSource()==btnListReservationCov){
            Parent fxml= FXMLLoader.load(getClass().getResource("listReservationCov.fxml"));
            gestionReservationCovPane.getChildren().removeAll();
            gestionReservationCovPane.getChildren().setAll(fxml);
        }
    }

}
