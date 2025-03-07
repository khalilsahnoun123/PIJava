package com.example.demo.controllers.controllers_taxi;


import com.example.demo.models.models_taxi.ReservationTaxi;
import com.example.demo.models.models_taxi.Vehicule;
import com.example.demo.services.service_taxi.ServiceReservationTaxi;
import com.example.demo.services.service_taxi.ServiceVehicule;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class updateReservationTaxiController implements Initializable {

    @FXML
    private Button btnClearReservationTaxi;

    @FXML
    private Button btnUpdateReservationTaxi;

    @FXML
    private ComboBox<String> txtStatusResCov;

    @FXML
    private ComboBox<String> txtVec;

    @FXML
    private AnchorPane updateReservationTaxiPane;


    ReservationTaxi restaxi;

    ServiceVehicule serviceVehicule = new ServiceVehicule();
    List<Vehicule> vehiculeList = serviceVehicule.Show();
    private int vecId=-1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/ressource_taxi/itemReservationTaxi.fxml"));
        try {
            AnchorPane anchorPane = fxmlLoader.load();
            HBox hBox = (HBox) anchorPane.getChildren().get(0);
            itemReservationTaxiController item = fxmlLoader.getController();
            ServiceReservationTaxi crs = new ServiceReservationTaxi();

            restaxi = crs.getById(item.getId());
            txtStatusResCov.setValue(restaxi.getStatus());
            txtVec.setValue(String.valueOf(serviceVehicule.getById(restaxi.getId_vehicule()).getMarque()));
            vecId=restaxi.getId_vehicule();

        } catch (IOException ex) {
            Logger.getLogger(itemReservationTaxiController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Map<String, Integer> valuesMap = new HashMap<>();
        for(Vehicule cov : vehiculeList){
            txtVec.getItems().add(cov.getMarque());
            valuesMap.put(cov.getMarque(),cov.getId());
        }

        txtVec.setOnAction(event ->{
            String SelectedOption = null;
            SelectedOption = txtVec.getValue();
            int SelectedValue = 0;
            SelectedValue = valuesMap.get(SelectedOption);
            vecId = SelectedValue;
        });

        txtStatusResCov.getItems().addAll("En Attente","Refusé","Accepté");
    }

    @FXML
    void UpdateReservationTaxi(ActionEvent event) {
        if (txtStatusResCov.getValue().isEmpty() || vecId == -1)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information manquante");
            alert.setHeaderText(null);
            alert.setContentText("Vous devez remplir tous les détails concernant votre Reservation Taxi.");
            Optional<ButtonType> option = alert.showAndWait();

        } else {
            modifReservationTaxi();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Modifié avec succès");
            alert.setHeaderText(null);
            alert.setContentText("Votre Reservation Taxi a été modifié avec succès.");
            Optional<ButtonType> option = alert.showAndWait();
        }
    }

    @FXML
    void clearFieldsReservationTaxi(ActionEvent event) {
        txtVec.getEditor().clear();
        txtStatusResCov.getEditor().clear();
    }

    void modifReservationTaxi(){
        int id_user = restaxi.getId_user();
        String status = txtStatusResCov.getValue();
        int id_vec = vecId;


        ReservationTaxi resTaxi = new ReservationTaxi(
                restaxi.getId(),
                id_vec, status, id_user);
        ServiceReservationTaxi crs = new ServiceReservationTaxi();
        crs.modifier(resTaxi);
    }

}
