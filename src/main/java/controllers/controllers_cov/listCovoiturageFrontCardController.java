package controllers.controllers_cov;

import models.models_cov.Covoiturage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class listCovoiturageFrontCardController implements Initializable {

    @FXML
    private Label labelDepartCov;

    @FXML
    private Label labelDestinationCov;

    @FXML
    private Label labelNbrPlaceCov;

    @FXML
    private Label labelPrixCov;


    Covoiturage cov;
    private static int idCov;

    public int getIdCov(){
        return this.idCov;
    }
    public void setData (Covoiturage cov){
        this.cov = cov;
        labelDepartCov.setText("Départ : "+cov.getPoint_de_depart());
        labelDestinationCov.setText("Destination : "+cov.getPoint_de_destination());
        labelPrixCov.setText("Prix : " +String.valueOf(cov.getPrix())+" DT");
        labelNbrPlaceCov.setText("Nombre Place : "+String.valueOf(cov.getNbr_place()));
        this.idCov=cov.getId();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void open_ajouterReservation(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addReservationCovFront.fxml"));
        Parent fxml = loader.load();

        // Get the controller of the next page
        addReservationCovFrontController controller = loader.getController();

        // Pass the id of the covoiturage
        controller.setIdCov(this.getIdCov());

        Stage stage = new Stage();
        stage.setTitle("Add Reservation | Covoiturage : " + this.getIdCov());
        stage.setScene(new Scene(fxml));
        stage.showAndWait();
    }
}
