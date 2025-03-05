package controllers.controllers_taxi;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;



import org.controlsfx.control.Notifications;
import javafx.geometry.Pos;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import services.service_taxi.FirebaseService;

public class gestionReservationTaxiController implements Initializable {
    @FXML
    private Button btnAddReservationTaxi;

    @FXML
    private Button btnListReservationTaxi;

    @FXML
    private AnchorPane gestionReservationTaxiPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void goToPages(ActionEvent event) throws IOException {
        if(event.getSource()== btnAddReservationTaxi){
            Parent fxml= FXMLLoader.load(getClass().getResource("/ressource_taxi/addReservationTaxi.fxml"));
            gestionReservationTaxiPane.getChildren().removeAll();
            gestionReservationTaxiPane.getChildren().setAll(fxml);
        }else if(event.getSource()==btnListReservationTaxi){
            Parent fxml= FXMLLoader.load(getClass().getResource("/ressource_taxi/listReservationTaxi.fxml"));
            gestionReservationTaxiPane.getChildren().removeAll();
            gestionReservationTaxiPane.getChildren().setAll(fxml);
        }
    }
    public void initialize() {
        // Écouter les nouvelles réservations dans Firebase
        DatabaseReference ref = FirebaseService.database.getReference("reservations");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Quand une nouvelle réservation est ajoutée, afficher la notification
                afficherNotification();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Gérer les erreurs
            }
        });
    }

    public void afficherNotification() {
        // Afficher une notification à l'admin
        Image img = new Image(getClass().getResourceAsStream("/img/notification.png"));
        ImageView imageView = new ImageView(img);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);

        Notifications.create()
                .title("Nouvelle Réservation")
                .text("Un utilisateur a réservé un taxi.")
                .graphic(imageView)
                .hideAfter(Duration.seconds(5))
                .position(Pos.TOP_RIGHT)
                .darkStyle()
                .show();
    }



}


