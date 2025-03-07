package com.example.demo.controllers.controllers_velo;

import com.example.demo.models.models_velo.StationVelo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import com.example.demo.services.services_velo.StationVeloService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ReserverVeloController implements Initializable {

    @FXML
    private ComboBox<String> governoratCombo;
    @FXML private ComboBox<String> municipaliteCombo;
    @FXML private TextField adresseField;
    @FXML private FlowPane stationsContainer;
    @FXML private Button buttonCov;
    @FXML private Button transportPublicButton;
    @FXML private Button taxibutton;
    @FXML private Button buttonlogout;
    private final StationVeloService stationService = new StationVeloService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadGovernorats();
        setupMunicipaliteListener();
    }
    public void buttonCovOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ressource_cov/listCovoiturageFront.fxml"));
        buttonCov.getScene().setRoot(root);
    }
    public void buttontaxiOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ressource_cov/listCovoiturageFront.fxml"));
        taxibutton.getScene().setRoot(root);
    }
    public void buttontransportOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Ressource-TP/AddReservation-TP.fxml"));
        transportPublicButton.getScene().setRoot(root);
    }
    public void buttonHomeOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/resource-Velo/home.fxml"));
        buttonCov.getScene().setRoot(root);
    }
    private void loadGovernorats() {
        governoratCombo.getItems().addAll(stationService.getAllGovernorats());
    }

    private void setupMunicipaliteListener() {
        governoratCombo.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    municipaliteCombo.getItems().clear();
                    if (newVal != null) {
                        municipaliteCombo.getItems().addAll(
                                stationService.getMunicipalitiesByGovernorat(newVal)
                        );
                    }
                }
        );
    }



    private void displayStations(List<StationVelo> stations) {
        stationsContainer.getChildren().clear();
        stations.forEach(station -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/resource-Velo/StationCard.fxml"));
                Parent card = loader.load();
                StationCardController controller = loader.getController();
                controller.setStation(station);
                stationsContainer.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void handleSearch(javafx.event.ActionEvent actionEvent) {
        List<StationVelo> stations = stationService.searchStations(
                governoratCombo.getValue(),
                municipaliteCombo.getValue(),
                adresseField.getText()
        );

        displayStations(stations);
    }


    public void buttonlogoutOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Ressource-user/login.fxml"));
        buttonlogout.getScene().setRoot(root);
    }
}