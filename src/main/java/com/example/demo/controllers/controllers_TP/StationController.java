package com.example.demo.controllers.controllers_TP;

import com.example.demo.models.models_TP.Station;
import com.example.demo.services.services_TP.StationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class StationController {

    @FXML
    private TextField tfNom;
    @FXML
    private TextField tfAdresse;
    @FXML
    private Label lbStations;

    private StationService serviceStation = new StationService();

    @FXML
    public void ajouterStation(ActionEvent actionEvent) {
        Station station = new Station();
        station.setNom(tfNom.getText());
        station.setAdresse(tfAdresse.getText());

        serviceStation.add(station);
        afficherStations();
    }

    @FXML
    public void afficherStations() {
        lbStations.setText(serviceStation.getAll().toString());
    }
}
