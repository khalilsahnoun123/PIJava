package controllers.controllers_taxi;

import models.models_taxi.Vehicule;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import services.service_taxi.ServiceVehicule;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class addVehiculeController implements Initializable {
    @FXML
    private AnchorPane addVehiculePane;

    @FXML
    private Button btnAddVehicule;

    @FXML
    private Button btnClearVehicule;

    @FXML
    private TextField txtImm;

    @FXML
    private TextField txtMarque;

    @FXML
    private TextField txtModele;

    @FXML
    private TextField txtNumSerie;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void AjoutVehicule(ActionEvent event) {
        //check if not empty
        if(event.getSource() == btnAddVehicule){
            if (txtNumSerie.getText().isEmpty() || txtModele.getText().isEmpty() || txtMarque.getText().isEmpty()
                    || txtImm.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Information manquante");
                alert.setHeaderText(null);
                alert.setContentText("Vous devez remplir tous les détails concernant votre Vehicule.");
                Optional<ButtonType> option = alert.showAndWait();

            } else {
                ajouterVehicule();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Ajouté avec succès");
                alert.setHeaderText(null);
                alert.setContentText("Votre Vehicule a été ajoutée avec succès.");
                Optional<ButtonType> option = alert.showAndWait();

                clearFieldsVehicule();
            }
        }
        if(event.getSource() == btnClearVehicule){
            clearFieldsVehicule();
        }
    }

    @FXML
    void clearFieldsVehicule() {
        txtImm.clear();
        txtMarque.clear();
        txtModele.clear();
        txtNumSerie.clear();
    }

    private void ajouterVehicule() {

        // From Formulaire
        int id_user = 1;
        String marque = txtMarque.getText();
        String modele = txtModele.getText();
        String immatriculation = txtImm.getText();
        String numero_de_serie = txtNumSerie.getText();


        Vehicule cov = new Vehicule(
                id_user, marque, modele, immatriculation, numero_de_serie);
        ServiceVehicule cs = new ServiceVehicule();
        System.out.println("displaying vehicule before service enter" + cov.getId_chauffeur() + cov.getImmatriculation() + cov.getMarque() + cov.getModele() + cov.getNumero_de_serie());

        cs.ajouter(cov);
    }

}
