package controllers.controllers_taxi;

//import com.mysql.cj.Session;
import models.models_taxi.ReservationTaxi;
import models.models_taxi.User;
import models.models_taxi.Vehicule;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import services.service_taxi.FirebaseService;
import services.service_taxi.ServiceReservationTaxi;
import services.service_taxi.ServiceVehicule;


import java.net.URL;
import java.util.*;
import java.util.Properties;

import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import com.twilio.rest.api.v2010.account.Message;
import com.google.firebase.database.DatabaseReference;


import services.service_taxi.UserService;


public class addReservationTaxiController implements Initializable {

    @FXML
    private AnchorPane addReservationTaxiPane;
    @FXML
    private Button btnAddReservationTaxi, btnClearReservationTaxi;
    @FXML
    private ComboBox<String> txtStatusResCov, txtVec;

    private final ServiceVehicule sv = new ServiceVehicule();
    private final List<Vehicule> vecs = sv.Show();
    private final Map<String, Integer> valuesMap = new HashMap<>();
    private int idVec = -1;
    private final UserService userService = new UserService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (Vehicule vec : vecs) {
            txtVec.getItems().add(vec.getMarque());
            valuesMap.put(vec.getMarque(), vec.getId());
        }

        txtVec.setOnAction(event -> idVec = valuesMap.get(txtVec.getValue()));
        txtStatusResCov.getItems().addAll("En Attente", "Refusé", "Accepté");
    }

    @FXML
    void AjoutReservationTaxi(ActionEvent event) {
        if (event.getSource() == btnAddReservationTaxi) {
            System.out.println("Ajout reservation taxi 2");

            if (idVec == -1 || txtStatusResCov.getValue() == null) {
                showAlert(Alert.AlertType.WARNING, "Information manquante", "Veuillez remplir tous les détails de la réservation.");
            } else {
                ajouterReservationTaxi();
                System.out.println("Ajout reservation taxi 3");

                showAlert(Alert.AlertType.INFORMATION, "Succès", "Votre réservation a été ajoutée avec succès.");
                         clearFieldsReservationTaxi();
            }
        } else if (event.getSource() == btnClearReservationTaxi) {
            clearFieldsReservationTaxi();
        }
    }

    @FXML
    void clearFieldsReservationTaxi() {
        txtVec.getSelectionModel().clearSelection();
        txtStatusResCov.getSelectionModel().clearSelection();
    }

    private void ajouterReservationTaxi() {
        System.out.println("Ajout reservation taxi 1 ");

        int id_user = 1;
        String status = txtStatusResCov.getValue();
        ReservationTaxi res_taxi = new ReservationTaxi(idVec, status, id_user);
        ServiceReservationTaxi rts = new ServiceReservationTaxi();
        send_sms();
        rts.ajouter(res_taxi);
    }



    /*void send_sms(){
        String ACCOUNT_SID = "ACb77418499a96c48513c7ed7c5c6a8837";
        String AUTH_TOKEN = "8a5c4c0b279e859a3b98dd4a701b62e7";

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        String recepientNumber = "+21695698847";
        String message = "Bonjour Mr, \n"
                +"Nous sommes ravis de vous informer qu'une reservation a été effectuée.\n"
                +"Veuillez contactez l'administration pour plus de details. \n"
                +"Merci de votre fidélité et à bientôt.\n"
                +"Cordialement, \n";

        com.twilio.rest.api.v2010.account.Message twilioMessage = com.twilio.rest.api.v2010.account.Message.creator(
                new PhoneNumber(recepientNumber),
                new PhoneNumber("+14064123283"), message).create();
        System.out.println("SMS envoyé : "+twilioMessage.getSid());
    }*/

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    void send_sms(){
        String ACCOUNT_SID = "ACb77418499a96c48513c7ed7c5c6a8837";
        String AUTH_TOKEN = "8a5c4c0b279e859a3b98dd4a701b62e7";

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        String recepientNumber = "+21695698847";
        String message = "Bonjour Mr, \n"
                +"Nous sommes ravis de vous informer qu'une reservation a été effectuée.\n"
                +"Veuillez contactez l'administration pour plus de details. \n"
                +"Merci de votre fidélité et à bientôt.\n"
                +"Cordialement, \n";

        Message twilioMessage = Message.creator(
                new PhoneNumber(recepientNumber),
                new PhoneNumber("+14064123283"),message).create();
        System.out.println("SMS envoyé : "+twilioMessage.getSid());
    }
}
