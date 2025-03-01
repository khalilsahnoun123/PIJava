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

public class gestionCovoiturageController implements Initializable {

    @FXML
    private Button btnAddCovoiturage;

    @FXML
    private Button btnListCovoiturage;

    @FXML
    private AnchorPane gestionCovoituragePane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void goToPages(ActionEvent event) throws IOException {
        if(event.getSource()== btnAddCovoiturage){
            Parent fxml= FXMLLoader.load(getClass().getResource("addCovoiturage.fxml"));
            gestionCovoituragePane.getChildren().removeAll();
            gestionCovoituragePane.getChildren().setAll(fxml);
        }else if(event.getSource()==btnListCovoiturage){
            Parent fxml= FXMLLoader.load(getClass().getResource("listCovoiturage.fxml"));
            gestionCovoituragePane.getChildren().removeAll();
            gestionCovoituragePane.getChildren().setAll(fxml);
        }
    }

}
