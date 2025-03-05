package controllers.controllers_cov;

import models.models_cov.ReservationCov;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.service_cov.ServiceReservationCov;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class listReservationCovController implements Initializable {

    @FXML
    private AnchorPane listReservationCovPane;

    @FXML
    private VBox vBox;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ServiceReservationCov src = new ServiceReservationCov();
            List<ReservationCov> rescovs = src.Show();

            for(int i=0;i<rescovs.size();i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/ressource_cov/itemReservationCov.fxml"));
                try {
                    AnchorPane anchorPane = fxmlLoader.load();
                    HBox hBox = (HBox) anchorPane.getChildren().get(0);
                    itemReservationCovController itemController = fxmlLoader.getController();
                    itemController.setData(rescovs.get(i));
                    vBox.getChildren().add(hBox);
                } catch (IOException ex) {
                    Logger.getLogger(itemReservationCovController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void open_Stat(ActionEvent event)throws IOException {
        Parent fxml= FXMLLoader.load(getClass().getResource("/ressource_cov/Statistiques.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Statistiques des Reservations Covoiturage");
        stage.setScene(new Scene(fxml));
        stage.showAndWait();
    }

}
