package controllers.controllers_taxi;

import models.models_taxi.Vehicule;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import services.service_taxi.ServiceVehicule;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class updateVehiculeController implements Initializable {
    @FXML
    private Button btnClearVehicule;

    @FXML
    private Button btnUpdateVehicule;

    @FXML
    private TextField txtImm;

    @FXML
    private TextField txtMarque;

    @FXML
    private TextField txtModele;

    @FXML
    private TextField txtNumSerie;

    @FXML
    private AnchorPane updateVehiculePane;


    Vehicule vec;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/ressource_taxi/itemVehicule.fxml"));
        try {
            AnchorPane anchorPane = fxmlLoader.load();
            HBox hBox = (HBox) anchorPane.getChildren().get(0);
            itemVehiculeController item = fxmlLoader.getController();
            ServiceVehicule cs = new ServiceVehicule();

            vec = cs.getById(item.getId());
            txtMarque.setText(vec.getMarque());
            txtModele.setText(vec.getModele());
            txtImm.setText(vec.getImmatriculation());
            txtNumSerie.setText(vec.getNumero_de_serie());

        } catch (IOException ex) {
            Logger.getLogger(itemVehiculeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void UpdateVehicule(ActionEvent event) {
        if (txtMarque.getText().isEmpty() || txtModele.getText().isEmpty() ||
                txtImm.getText().isEmpty() || txtNumSerie.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information manquante");
            alert.setHeaderText(null);
            alert.setContentText("Vous devez remplir tous les détails concernant votre Vehicule.");
            Optional<ButtonType> option = alert.showAndWait();

        } else {
            modifVehicule();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Modifié avec succès");
            alert.setHeaderText(null);
            alert.setContentText("Votre Vehicule a été modifié avec succès.");
            Optional<ButtonType> option = alert.showAndWait();
        }
    }

    @FXML
    void clearFieldsVehicule() {
        txtImm.clear();
        txtNumSerie.clear();
        txtModele.clear();
        txtMarque.clear();
    }

    void modifVehicule(){
        int id_chauffeur = vec.getId_chauffeur();
        String marque = txtMarque.getText();
        String modele = txtModele.getText();
        String imm = txtImm.getText();
        String numserie = txtNumSerie.getText();


        Vehicule c = new Vehicule(
                vec.getId(),
                id_chauffeur, marque, modele, imm, numserie);
        ServiceVehicule cs = new ServiceVehicule();
        cs.modifier(c);
    }
}
