package controllers.controllers_taxi;

import models.models_taxi.Reservation;


import models.models_taxi.ReservationTaxi;
import models.models_taxi.Vehicule;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import services.service_taxi.FirebaseService;
import services.service_taxi.ServiceReservationTaxi;
import services.service_taxi.ServiceVehicule;



import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import com.twilio.rest.api.v2010.account.Message;
import com.google.firebase.database.DatabaseReference;



import java.net.URL;
import java.util.*;


public class addReservationTaxiController implements Initializable {

    @FXML
    private AnchorPane addReservationTaxiPane;

    @FXML
    private Button btnAddReservationTaxi;

    @FXML
    private Button btnClearReservationTaxi;

    @FXML
    private ComboBox<String> txtStatusResCov;

    @FXML
    private ComboBox<String> txtVec;


    ServiceVehicule sv = new ServiceVehicule();
    List<Vehicule> vecs = sv.Show();
    private int idVec=-1;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Map<String, Integer> valuesMap = new HashMap<>();
        for(Vehicule vec : vecs){
            txtVec.getItems().add(vec.getMarque());
            valuesMap.put(vec.getMarque(),vec.getId());
        }

        txtVec.setOnAction(event ->{
            String SelectedOption = null;
            SelectedOption = txtVec.getValue();
            int SelectedValue = 0;
            SelectedValue = valuesMap.get(SelectedOption);
            idVec = SelectedValue;
        });

        txtStatusResCov.getItems().addAll("En Attente","Refusé","Accepté");
    }
    public void reserverTaxi(String taxiId, String userId) {
        // Enregistrer une nouvelle réservation dans Firebase
        DatabaseReference ref = FirebaseService.database.getReference("reservations");
        String reservationId = ref.push().getKey();  // Générer un identifiant unique
        ref.child(reservationId).setValueAsync(new Reservation(taxiId, userId));

        // Notifier l'admin via la méthode de notification après la réservation
        afficherNotification();
    }

    public void afficherNotification() {
        // Afficher une notification (voir la méthode précédemment décrite)
    }

    @FXML
    void AjoutReservationTaxi(ActionEvent event) {
        //check if not empty
        if(event.getSource() == btnAddReservationTaxi){
            if (idVec==-1)
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Information manquante");
                alert.setHeaderText(null);
                alert.setContentText("Vous devez remplir tous les détails concernant votre Reservation.");
                Optional<ButtonType> option = alert.showAndWait();

            } else {
                ajouterReservationTaxi();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Ajouté avec succès");
                alert.setHeaderText(null);
                alert.setContentText("Votre Reservation a été ajoutée avec succès.");
                Optional<ButtonType> option = alert.showAndWait();
                //send_sms();
                clearFieldsReservationTaxi();
            }
        }
        if(event.getSource() == btnClearReservationTaxi){
            clearFieldsReservationTaxi();
        }
    }

    @FXML
    void clearFieldsReservationTaxi() {
        txtVec.getEditor().clear();
        txtStatusResCov.getEditor().clear();
    }

    private void ajouterReservationTaxi() {

        // From Formulaire
        int id_user = 1;
        int id_vec = idVec;
        String status = txtStatusResCov.getValue();


        ReservationTaxi res_taxi = new ReservationTaxi(
                id_user, status, id_vec);
        ServiceReservationTaxi rts = new ServiceReservationTaxi();
        rts.ajouter(res_taxi);
    }


}




