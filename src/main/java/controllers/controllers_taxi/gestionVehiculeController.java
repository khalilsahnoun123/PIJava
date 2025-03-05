package controllers.controllers_taxi;

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

public class gestionVehiculeController implements Initializable {
    @FXML
    private Button btnAddVehicule;

    @FXML
    private Button btnListVehicule;

    @FXML
    private AnchorPane gestionVehiculePane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void goToPages(ActionEvent event) throws IOException {
        if(event.getSource()== btnAddVehicule){
            Parent fxml= FXMLLoader.load(getClass().getResource("/ressource_taxi/addVehicule.fxml"));
            gestionVehiculePane.getChildren().removeAll();
            gestionVehiculePane.getChildren().setAll(fxml);
        }else if(event.getSource()==btnListVehicule){
            Parent fxml= FXMLLoader.load(getClass().getResource("/ressource_taxi/listVehicule.fxml"));
            gestionVehiculePane.getChildren().removeAll();
            gestionVehiculePane.getChildren().setAll(fxml);
        }
    }
}
