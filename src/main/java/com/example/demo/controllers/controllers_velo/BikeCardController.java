package com.example.demo.controllers.controllers_velo;

import com.example.demo.models.models_velo.Velo;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class BikeCardController {
    @FXML private ImageView bikeImage;
    @FXML private Label typeLabel;
    @FXML private Label pricePerHourLabel;
    @FXML private Label statusLabel;

    private Velo currentBike;
    private BikeSelectionController parentController;

    public void setBikeData(Velo bike) {
        this.currentBike = bike;
        updateUI();
    }

    private void updateUI() {
        try {
            bikeImage.setImage(new Image(new File(currentBike.getType().getVeloImage()).toURI().toString()));
        } catch (Exception e) {
            bikeImage.setImage(new Image("/images/default_bike.png"));
        }

        typeLabel.setText(currentBike.getType().getTypeName());
        pricePerHourLabel.setText(String.format("Prix/h: %.2f DT", currentBike.getType().getPrice()));
        statusLabel.setText(currentBike.isDispo() ? "Disponible" : "Réservé");
    }

    public void setParentController(BikeSelectionController parentController) {
        this.parentController = parentController;
        System.out.println("Parent controller set to: " + parentController); // Debug
    }

    @FXML
    private void handleReservation() {
        System.out.println("Bouton cliqué - Vélo ID: " + currentBike.getIdVelo());

        if (parentController != null) {
            System.out.println("Appel de initReservation...");
            parentController.initReservation(currentBike);
        } else {
            System.err.println("Erreur: parentController est null !");
        }
    }
}