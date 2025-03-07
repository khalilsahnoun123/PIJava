package com.example.demo.controllers.controllers_taxi;


import com.example.demo.models.models_taxi.ReservationTaxi;
import com.example.demo.services.service_taxi.ServiceReservationTaxi;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class listReservationTaxiController implements Initializable {

    @FXML
    private AnchorPane listReservationTaxiPane;

    @FXML
    private VBox vBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ServiceReservationTaxi src = new ServiceReservationTaxi();
            List<ReservationTaxi> reservationTaxiList = src.Show();

            for(int i=0;i<reservationTaxiList.size();i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/ressource_taxi/itemReservationTaxi.fxml"));
                try {
                    AnchorPane anchorPane = fxmlLoader.load();
                    HBox hBox = (HBox) anchorPane.getChildren().get(0);
                    itemReservationTaxiController itemController = fxmlLoader.getController();
                    itemController.setData(reservationTaxiList.get(i));
                    vBox.getChildren().add(hBox);
                } catch (IOException ex) {
                    Logger.getLogger(itemReservationTaxiController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
