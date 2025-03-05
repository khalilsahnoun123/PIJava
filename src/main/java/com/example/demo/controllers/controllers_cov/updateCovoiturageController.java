package com.example.demo.controllers.controllers_cov;

import com.example.demo.models.models_cov.Covoiturage;
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
import com.example.demo.services.service_cov.ServiceCovoiturage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class updateCovoiturageController implements Initializable {
    @FXML
    private Button btnClearCovoiturage;

    @FXML
    private Button btnUpdateCovoiturage;

    @FXML
    private TextField txtNbrPlace;

    @FXML
    private TextField txtPointDepart;

    @FXML
    private TextField txtPointDestination;

    @FXML
    private TextField txtPrix;

    @FXML
    private AnchorPane updateCovoituragePane;

    Covoiturage cov;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/ressource_cov/itemCovoiturage.fxml"));
        try {
            AnchorPane anchorPane = fxmlLoader.load();
            HBox hBox = (HBox) anchorPane.getChildren().get(0);
            itemCovoiturageController item = fxmlLoader.getController();
            ServiceCovoiturage cs = new ServiceCovoiturage();

            cov = cs.getById(item.getId());
            txtPointDepart.setText(cov.getPoint_de_depart());
            txtPointDestination.setText(cov.getPoint_de_destination());
            txtPrix.setText(String.valueOf(cov.getPrix()));
            txtNbrPlace.setText(String.valueOf(cov.getNbr_place()));

        } catch (IOException ex) {
            Logger.getLogger(itemCovoiturageController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void UpdateCovoiturage(ActionEvent event) {
        if (txtPrix.getText().isEmpty() || txtNbrPlace.getText().isEmpty() ||
                txtPointDestination.getText().isEmpty() || txtPointDepart.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information manquante");
            alert.setHeaderText(null);
            alert.setContentText("Vous devez remplir tous les détails concernant votre Covoiturage.");
            Optional<ButtonType> option = alert.showAndWait();

        } else {
            modifCovoiturage();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Modifié avec succès");
            alert.setHeaderText(null);
            alert.setContentText("Votre Covoiturage a été modifié avec succès.");
            Optional<ButtonType> option = alert.showAndWait();
        }
    }

    @FXML
    void clearFieldsCovoiturage(ActionEvent event) {
        txtPrix.clear();
        txtPointDepart.clear();
        txtPointDestination.clear();
        txtNbrPlace.clear();
    }

    void modifCovoiturage(){
        int id_user = cov.getId_user();
        String point_de_depart = txtPointDepart.getText();
        String point_de_destination = txtPointDestination.getText();
        float prix = Float.parseFloat(txtPrix.getText());
        int nbr_place = Integer.parseInt(txtNbrPlace.getText());


        Covoiturage c = new Covoiturage(
                cov.getId(),
                id_user, point_de_depart, point_de_destination, prix, nbr_place);
        ServiceCovoiturage cs = new ServiceCovoiturage();
        cs.modifier(c);
    }


}
