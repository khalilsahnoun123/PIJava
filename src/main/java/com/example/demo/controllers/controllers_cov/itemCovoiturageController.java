package com.example.demo.controllers.controllers_cov;

import com.example.demo.models.models_cov.Covoiturage;
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
import com.example.demo.services.service_cov.ServiceCovoiturage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class itemCovoiturageController implements Initializable {

    @FXML
    private Button btnModifierCovoiturage;

    @FXML
    private Button btnSupprimerCovoiturage;

    @FXML
    private AnchorPane itemCovoituragePane;

    @FXML
    private Label labelNbrPlace;

    @FXML
    private Label labelPointDepart;

    @FXML
    private Label labelPointDestination;

    @FXML
    private Label labelPrix;


    private static int id;

    public int getId(){
        return this.id;
    }

    Covoiturage cov;
    public void setData (Covoiturage cov){
        this.cov = cov;

        labelPointDepart.setText(cov.getPoint_de_depart());
        labelPointDestination.setText(cov.getPoint_de_destination());
        labelPrix.setText(String.valueOf(cov.getPrix())+" DT");
        labelNbrPlace.setText(String.valueOf(cov.getNbr_place()));
        this.id=cov.getId();
    }

    public Covoiturage getData (Covoiturage cov){
        this.cov = cov;
        return this.cov;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void open_UpdateCovoiturage(ActionEvent event) throws IOException {
        Parent fxml= FXMLLoader.load(getClass().getResource("/ressource_cov/updateCovoiturage.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Update Covoiturage");
        stage.setScene(new Scene(fxml));
        stage.showAndWait();
    }

    @FXML
    void supprimerCovoiturage(ActionEvent event) throws SQLException {
        ServiceCovoiturage cs = new ServiceCovoiturage();

        // Afficher une boîte de dialogue de confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Voulez-vous vraiment supprimer ce Covoiturage ?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Récupérer l'ID de Covoiturage sélectionnée
            int id = this.cov.getId();

            // Supprimer le Covoiturage de la base de données
            cs.supprimer(id);
        }
    }

}
