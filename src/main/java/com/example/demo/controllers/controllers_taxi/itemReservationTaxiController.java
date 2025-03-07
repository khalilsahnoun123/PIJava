package com.example.demo.controllers.controllers_taxi;


import com.example.demo.models.models_taxi.ReservationTaxi;
import com.example.demo.services.service_taxi.ServiceReservationTaxi;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class itemReservationTaxiController implements Initializable {

    @FXML
    private Button btnModifierReservationTaxi;

    @FXML
    private Button btnSupprimerReservationTaxi;

    @FXML
    private AnchorPane itemReservationTaxiPane;

    @FXML
    private Label labelStatus;

    @FXML
    private Label labelUtilisateur;

    @FXML
    private Label labelVehicule;

    private static int id;

    public int getId(){
        return this.id;
    }

    ReservationTaxi restaxi;
    public void setData (ReservationTaxi restaxi){
        this.restaxi = restaxi;

        labelStatus.setText(restaxi.getStatus());
        labelUtilisateur.setText(String.valueOf(restaxi.getId_user()));
        labelVehicule.setText(String.valueOf(restaxi.getId_vehicule()));
        this.id=restaxi.getId();
    }

    public ReservationTaxi getData (ReservationTaxi restaxi){
        this.restaxi = restaxi;
        return this.restaxi;
    }

    @FXML
    void open_UpdateReservationTaxi(ActionEvent event) throws IOException {
        Parent fxml= FXMLLoader.load(getClass().getResource("/ressource_taxi/updateReservationTaxi.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Update Reservation Taxi");
        stage.setScene(new Scene(fxml));
        stage.showAndWait();
    }

    @FXML
    void supprimerReservationTaxi(ActionEvent event) throws SQLException {
        ServiceReservationTaxi crs = new ServiceReservationTaxi();

        // Afficher une boîte de dialogue de confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Voulez-vous vraiment supprimer cette Réservation Taxi ?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Récupérer l'ID de la reservation sélectionnée
            int id = this.restaxi.getId();

            // Supprimer la reservation de la base de données
            crs.supprimer(id);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
