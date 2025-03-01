package controllers.controllers_cov;

import models.models_cov.ReservationCov;
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
import services.service_cov.ServiceReservationCov;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class itemReservationCovController implements Initializable {

    @FXML
    private Button btnModifierReservationCov;

    @FXML
    private Button btnSupprimerReservationCov;

    @FXML
    private AnchorPane itemReservationCovPane;

    @FXML
    private Label labelCov;

    @FXML
    private Label labelNbrPlace;

    @FXML
    private Label labelStatus;

    private static int id;

    public int getId(){
        return this.id;
    }

    ReservationCov rescov;
    public void setData (ReservationCov rescov){
        this.rescov = rescov;

        labelStatus.setText(rescov.getStatus());
        labelNbrPlace.setText(String.valueOf(rescov.getNbr_place()));
        labelCov.setText(String.valueOf(rescov.getId_cov()));
        this.id=rescov.getId();
    }

    public ReservationCov getData (ReservationCov rescov){
        this.rescov = rescov;
        return this.rescov;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void open_UpdateReservationCov(ActionEvent event) throws IOException {
        Parent fxml= FXMLLoader.load(getClass().getResource("updateReservationCov.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Update Reservation Covoiturage");
        stage.setScene(new Scene(fxml));
        stage.showAndWait();
    }

    @FXML
    void supprimerReservationCov(ActionEvent event) throws SQLException {
        ServiceReservationCov crs = new ServiceReservationCov();

        // Afficher une boîte de dialogue de confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Voulez-vous vraiment supprimer cette Réservation Covoiturage ?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Récupérer l'ID de la reservation sélectionnée
            int id = this.rescov.getId();

            // Supprimer la reservation de la base de données
            crs.supprimer(id);
        }

    }


}
