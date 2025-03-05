package controllers.controllers_cov;

import models.models_cov.Covoiturage;
import models.models_cov.ReservationCov;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import services.service_cov.ServiceCovoiturage;
import services.service_cov.ServiceReservationCov;

import java.net.URL;
import java.util.*;

public class addReservationCovController implements Initializable {

    @FXML
    private AnchorPane addReservationCovPane;

    @FXML
    private Button btnAddReservationCov;

    @FXML
    private Button btnClearReservationCov;

    @FXML
    private TextField txtNbrPlace;

    @FXML
    private ComboBox<String> txtStatusResCov;

    @FXML
    private ComboBox<String> txtCovoiturage;

    ServiceCovoiturage sc = new ServiceCovoiturage();
    List<Covoiturage> covs = sc.Show();
    private int idCov=-1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Map<String, Integer> valuesMap = new HashMap<>();
        for(Covoiturage c : covs){
            txtCovoiturage.getItems().add(c.getPoint_de_depart());
            valuesMap.put(c.getPoint_de_depart(),c.getId());
        }

        txtCovoiturage.setOnAction(event ->{
            String SelectedOption = null;
            SelectedOption = txtCovoiturage.getValue();
            int SelectedValue = 0;
            SelectedValue = valuesMap.get(SelectedOption);
            idCov = SelectedValue;
        });

        txtStatusResCov.getItems().addAll("En Attente","Refusé","Accepté");
    }

    @FXML
    void AjoutReservationCov(ActionEvent event) {
        //check if not empty
        if(event.getSource() == btnAddReservationCov){
            if (idCov==-1 || txtNbrPlace.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Information manquante");
                alert.setHeaderText(null);
                alert.setContentText("Vous devez remplir tous les détails concernant votre Reservation.");
                Optional<ButtonType> option = alert.showAndWait();

            } else {
                ajouterReservationCov();
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
        txtStatusResCov.getItems().isEmpty();
        txtNbrPlace.clear();
    }

    private void ajouterReservationCov() {

        // From Formulaire
        int id_user = 1;
        int id_cov = idCov;
        String status = txtStatusResCov.getValue();
        int nbr_place = Integer.parseInt(txtNbrPlace.getText());


        ReservationCov res_cov = new ReservationCov(
                id_user, id_cov, status, nbr_place);
        ServiceReservationCov rcs = new ServiceReservationCov();
        rcs.ajouter(res_cov);
    }


}
