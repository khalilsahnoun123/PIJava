package com.example.demo.controllers.controllers_TP;

import com.example.demo.enums.enums_TP.Region;
import com.example.demo.models.models_TP.Ligne;
import com.example.demo.services.services_TP.LigneService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class AddLigneController {

    @FXML
    private TextField nameField;
    @FXML
    private Label nameErrorLabel;

    @FXML
    private ComboBox<Region> regionField;
    @FXML
    private Label regionErrorLabel;

    @FXML
    private TextField prixVIPField;
    @FXML
    private Label prixVIPErrorLabel;

    @FXML
    private TextField prixPremiumField;
    @FXML
    private Label prixPremiumErrorLabel;

    @FXML
    private TextField prixEconoField;
    @FXML
    private Label prixEconoErrorLabel;

    @FXML
    private Label successLabel;

    @FXML
    private Button previousButton;

    private LigneService ligneService = new LigneService();

    @FXML
    public void initialize() {
        // Populate Region ComboBox
        regionField.getItems().setAll(Region.values());
    }

    @FXML
    public void saveLigne() {
        successLabel.setVisible(false);
        boolean isValid = validateForm();

        if (isValid) {
            try {
                Ligne ligne = new Ligne();
                ligne.setName(nameField.getText());
                ligne.setRegion(regionField.getValue());
                ligne.setPrixVIP(Double.parseDouble(prixVIPField.getText()));
                ligne.setPrixPREMIUM(Double.parseDouble(prixPremiumField.getText()));
                ligne.setPrixECONIMIC(Double.parseDouble(prixEconoField.getText()));

                ligneService.add(ligne);

                // Show success message and clear form
                successLabel.setVisible(true);
                clearForm();

                // Hide success message after 3 seconds
                new Thread(() -> {
                    try {
                        Thread.sleep(3000);
                        successLabel.setVisible(false);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();

            } catch (NumberFormatException e) {
                showErrorAlert("Entrée invalide", "Veuillez entrer des nombres valides pour les prix.");
            } catch (Exception e) {
                showErrorAlert("Erreur", "Erreur lors de l'enregistrement: " + e.getMessage());
            }
        }
    }

    @FXML
    private void goBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Ressource-TP/LigneManagement.fxml"));
            Stage stage = (Stage) previousButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestion des Lignes");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validateForm() {
        boolean isValid = true;
        resetErrorLabels();

        if (nameField.getText().isEmpty()) {
            nameErrorLabel.setVisible(true);
            isValid = false;
        }
        if (regionField.getValue() == null) {
            regionErrorLabel.setVisible(true);
            isValid = false;
        }
        if (prixVIPField.getText().isEmpty()) {
            prixVIPErrorLabel.setVisible(true);
            isValid = false;
        }
        if (prixPremiumField.getText().isEmpty()) {
            prixPremiumErrorLabel.setVisible(true);
            isValid = false;
        }
        if (prixEconoField.getText().isEmpty()) {
            prixEconoErrorLabel.setVisible(true);
            isValid = false;
        }

        return isValid;
    }

    private void resetErrorLabels() {
        nameErrorLabel.setVisible(false);
        regionErrorLabel.setVisible(false);
        prixVIPErrorLabel.setVisible(false);
        prixPremiumErrorLabel.setVisible(false);
        prixEconoErrorLabel.setVisible(false);
    }

    private void clearForm() {
        nameField.clear();
        regionField.getSelectionModel().clearSelection();
        prixVIPField.clear();
        prixPremiumField.clear();
        prixEconoField.clear();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}