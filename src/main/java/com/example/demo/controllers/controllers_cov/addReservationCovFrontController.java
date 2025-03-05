package com.example.demo.controllers.controllers_cov;

import com.example.demo.models.models_cov.ReservationCov;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import com.example.demo.services.service_cov.ServiceReservationCov;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class addReservationCovFrontController implements Initializable {
    @FXML
    private AnchorPane addReservationCovPane;

    @FXML
    private Button btnAddReservationCov;

    @FXML
    private Button btnClearReservationCov;

    @FXML
    private TextField txtNbrPlace;

    private int idCov;

    public void setIdCov(int idCov) {
        this.idCov = idCov;
        System.out.println("Received idCov: " + idCov); // Debugging purpose
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void AjoutReservationCov(ActionEvent event) {
        //check if not empty
        if(event.getSource() == btnAddReservationCov){
            if (txtNbrPlace.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Information manquante");
                alert.setHeaderText(null);
                alert.setContentText("Vous devez remplir tous les détails concernant votre Reservation.");
                Optional<ButtonType> option = alert.showAndWait();

            } else {
                // From Formulaire
                int id_user = 1;
                int id_cov = idCov;
                String status = "En Attente";
                int nbr_place = Integer.parseInt(txtNbrPlace.getText());


                ReservationCov res_cov = new ReservationCov(
                        id_user, id_cov, status, nbr_place);
                ServiceReservationCov rcs = new ServiceReservationCov();
                rcs.ajouter(res_cov);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Ajouté avec succès");
                alert.setHeaderText(null);
                alert.setContentText("Votre Reservation a été ajoutée avec succès.");
                Optional<ButtonType> option = alert.showAndWait();

                clearFieldsReservationCov();
            }
        }
        if(event.getSource() == btnClearReservationCov){
            clearFieldsReservationCov();
        }


    }

    @FXML
    void clearFieldsReservationCov() {
        txtNbrPlace.clear();
    }


}
