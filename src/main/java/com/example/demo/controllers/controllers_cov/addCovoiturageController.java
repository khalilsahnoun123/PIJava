package com.example.demo.controllers.controllers_cov;

import com.example.demo.models.models_cov.Covoiturage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import com.example.demo.services.service_cov.ServiceCovoiturage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class addCovoiturageController implements Initializable {

    @FXML
    private AnchorPane addCovoituragePane;

    @FXML
    private Button btnAddCovoiturage;

    @FXML
    private Button btnClearCovoiturage;

    @FXML
    private TextField txtNbrPlace;

    @FXML
    private TextField txtPointDepart;

    @FXML
    private TextField txtPointDestination;

    @FXML
    private TextField txtPrix;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void AjoutCovoiturage(ActionEvent event) {
        //check if not empty
        if(event.getSource() == btnAddCovoiturage){
            if (txtNbrPlace.getText().isEmpty() || txtPrix.getText().isEmpty() || txtPointDepart.getText().isEmpty()
                || txtPointDestination.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Information manquante");
                alert.setHeaderText(null);
                alert.setContentText("Vous devez remplir tous les détails concernant votre Covoiturage.");
                Optional<ButtonType> option = alert.showAndWait();

            } else {
                ajouterCovoiturage();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Ajouté avec succès");
                alert.setHeaderText(null);
                alert.setContentText("Votre Covoiturage a été ajoutée avec succès.");
                Optional<ButtonType> option = alert.showAndWait();

                clearFieldsCovoiturage();
            }
        }
        if(event.getSource() == btnClearCovoiturage){
            clearFieldsCovoiturage();
        }
    }

    @FXML
    void clearFieldsCovoiturage() {
        txtPointDepart.clear();
        txtPointDestination.clear();
        txtPrix.clear();
        txtNbrPlace.clear();
    }

    private void ajouterCovoiturage() {

        // From Formulaire
        int id_user = 1;
        String point_de_depart = txtPointDepart.getText();
        String point_de_destination = txtPointDestination.getText();
        float prix = Float.parseFloat(txtPrix.getText());
        int nbr_place = Integer.parseInt(txtNbrPlace.getText());


        Covoiturage cov = new Covoiturage(
                id_user, point_de_depart, point_de_destination, prix, nbr_place);
        ServiceCovoiturage cs = new ServiceCovoiturage();
        cs.ajouter(cov);
    }

}
