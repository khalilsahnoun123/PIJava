package com.example.demo.controllers.controllers_velo;
import com.example.demo.models.models_velo.StationVelo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import com.example.demo.services.services_velo.StationVeloService;

import java.io.IOException;

public class StationCardController {

    @FXML private Label nomStation;
    @FXML private Label adresseStation;
    @FXML private Label velosDisponibles;

    private StationVelo station;
    private StationVeloService serviceStation = new StationVeloService();

    public void setStation(StationVelo station) {
        this.station = station;
        updateUI();
    }

    private void updateUI() {
        nomStation.setText(station.getNameStation());
        adresseStation.setText(station.getAdresse());
        int availableBikes = serviceStation.getAvailableBikesCount(station.getIdStation());
        velosDisponibles.setText("Vélos disponibles : " + availableBikes);    }

    @FXML
    public void handleSelection(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resource-Velo/BikeSelection.fxml"));
            Parent root = loader.load();

            BikeSelectionController controller = loader.getController();
            controller.initializeWithStation(station);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}