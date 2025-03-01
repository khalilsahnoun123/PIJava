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

public class ModifyLigneController {

    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private Label nameErrorLabel;
    @FXML private ComboBox<Region> regionField;
    @FXML private Label regionErrorLabel;
    @FXML private TextField prixVIPField;
    @FXML private Label prixVIPErrorLabel;
    @FXML private TextField prixPremiumField;
    @FXML private Label prixPremiumErrorLabel;
    @FXML private TextField prixEconoField;
    @FXML private Label prixEconoErrorLabel;

    @FXML private Label successLabel;
    @FXML private Button previousButton;

    private LigneService ligneService = new LigneService();
    private Ligne ligneToModify;

    // Method to set the ligne to modify and populate the form
    public void setLigneToModify(Ligne ligne) {
        this.ligneToModify = ligne;
        populateForm();
    }

    @FXML
    public void initialize() {
        // Populate Region ComboBox
        regionField.getItems().setAll(Region.values());
    }

    private void populateForm() {
        if (ligneToModify != null) {
            idField.setText(String.valueOf(ligneToModify.getId()));
            nameField.setText(ligneToModify.getName());
            regionField.setValue(ligneToModify.getRegion());
            prixVIPField.setText(String.valueOf(ligneToModify.getPrixVIP()));
            prixPremiumField.setText(String.valueOf(ligneToModify.getPrixPREMIUM()));
            prixEconoField.setText(String.valueOf(ligneToModify.getPrixECONIMIC()));
        }
    }

    @FXML
    public void updateLigne() {
        successLabel.setVisible(false);
        boolean isValid = validateForm();

        if (isValid) {
            try {
                ligneToModify.setName(nameField.getText());
                ligneToModify.setRegion(regionField.getValue());
                ligneToModify.setPrixVIP(Double.parseDouble(prixVIPField.getText()));
                ligneToModify.setPrixPREMIUM(Double.parseDouble(prixPremiumField.getText()));
                ligneToModify.setPrixECONIMIC(Double.parseDouble(prixEconoField.getText()));

                ligneService.update(ligneToModify);

                // Show success message
                successLabel.setVisible(true);

                // Redirect to LigneManagement after 2 seconds
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                        goToLigneManagement();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();

            } catch (NumberFormatException e) {
                showErrorAlert("Entrée invalide", "Veuillez entrer des nombres valides pour les prix.");
            } catch (Exception e) {
                showErrorAlert("Erreur", "Erreur lors de la modification: " + e.getMessage());
            }
        }
    }

    @FXML
    private void goBack() {
        goToLigneManagement();
    }

    private void goToLigneManagement() {
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
        if (prixVIPField.getText().isEmpty() || !isNumeric(prixVIPField.getText())) {
            prixVIPErrorLabel.setVisible(true);
            isValid = false;
        }
        if (prixPremiumField.getText().isEmpty() || !isNumeric(prixPremiumField.getText())) {
            prixPremiumErrorLabel.setVisible(true);
            isValid = false;
        }
        if (prixEconoField.getText().isEmpty() || !isNumeric(prixEconoField.getText())) {
            prixEconoErrorLabel.setVisible(true);
            isValid = false;
        }

        return isValid;
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void resetErrorLabels() {
        nameErrorLabel.setVisible(false);
        regionErrorLabel.setVisible(false);
        prixVIPErrorLabel.setVisible(false);
        prixPremiumErrorLabel.setVisible(false);
        prixEconoErrorLabel.setVisible(false);
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}