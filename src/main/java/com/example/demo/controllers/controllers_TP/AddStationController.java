package com.example.demo.controllers.controllers_TP;

import com.example.demo.models.models_TP.Ligne;
import com.example.demo.models.models_TP.Station;
import com.example.demo.services.services_TP.LigneService;
import com.example.demo.services.services_TP.StationService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class AddStationController {

    @FXML private TextField nomField;
    @FXML private TextField adresseField;
    @FXML private ComboBox<Ligne> ligneComboBox;
    @FXML private Button saveButton;
    @FXML private Button previousButton;
    @FXML private Label messageLabel;

    private StationService stationService = new StationService();
    private LigneService ligneService = new LigneService();

    @FXML
    public void initialize() {
        // Populate ligneComboBox
        ligneComboBox.setItems(FXCollections.observableArrayList(ligneService.getAll()));
        ligneComboBox.setCellFactory(param -> new ListCell<Ligne>() {
            @Override
            protected void updateItem(Ligne item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
        ligneComboBox.setButtonCell(new ListCell<Ligne>() {
            @Override
            protected void updateItem(Ligne item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });

        // Check if lignes are available
        if (ligneComboBox.getItems().isEmpty()) {
            showErrorMessage("Aucune ligne disponible. Veuillez en ajouter d'abord.");
            saveButton.setDisable(true);
        }

        // Add real-time input validation
        addTextValidation(nomField, "Le nom de la station est requis.", 50);
        addTextValidation(adresseField, "L'adresse est requise.", 100);
        ligneComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) {
                ligneComboBox.setStyle("-fx-border-color: red;");
            } else {
                ligneComboBox.setStyle("-fx-border-color: #bdc3c7; -fx-background-radius: 8;");
            }
        });
    }

    @FXML
    private void saveStation() {
        try {
            if (!validateFields()) {
                return;
            }

            Station station = new Station();
            station.setNom(nomField.getText().trim());
            station.setAdresse(adresseField.getText().trim());
            Ligne selectedLigne = ligneComboBox.getValue();
            if (selectedLigne == null || selectedLigne.getId() <= 0) {
                throw new IllegalStateException("Ligne invalide ou non sélectionnée.");
            }
            station.setLigne(selectedLigne);

            // Debug output
            System.out.println("Saving station: " + station.getNom() + ", " + station.getAdresse() + ", Ligne ID: " + station.getLigne().getId());

            stationService.add(station);
            showSuccessMessage("Station ajoutée avec succès!");
            goBack();
        } catch (Exception e) {
            showErrorMessage("Erreur lors de l'ajout: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void goBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Ressource-TP/StationManagement.fxml"));
            Stage stage = (Stage) previousButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestion des Stations");
        } catch (IOException e) {
            showErrorMessage("Erreur lors du retour: " + e.getMessage());
        }
    }

    private void addTextValidation(TextField field, String errorMessage, int maxLength) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isEmpty()) {
                field.setStyle("-fx-border-color: red;");
            } else if (newValue.length() > maxLength) {
                field.setText(newValue.substring(0, maxLength));
                field.setStyle("-fx-border-color: orange;");
                showErrorMessage("Maximum " + maxLength + " caractères pour " + field.getPromptText());
            } else {
                field.setStyle("-fx-border-color: #bdc3c7; -fx-background-radius: 8;");
            }
        });
    }

    private boolean validateFields() {
        if (nomField.getText().trim().isEmpty()) {
            showErrorMessage("Le nom de la station est requis.");
            nomField.setStyle("-fx-border-color: red;");
            return false;
        }
        if (adresseField.getText().trim().isEmpty()) {
            showErrorMessage("L'adresse est requise.");
            adresseField.setStyle("-fx-border-color: red;");
            return false;
        }
        if (ligneComboBox.getValue() == null) {
            showErrorMessage("Veuillez sélectionner une ligne.");
            ligneComboBox.setStyle("-fx-border-color: red;");
            return false;
        }
        return true;
    }

    private void showSuccessMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setTextFill(javafx.scene.paint.Color.GREEN);
        messageLabel.setStyle("-fx-background-color: #d4edda; -fx-text-fill: #155724; -fx-background-radius: 5;");
        messageLabel.setVisible(true);
        hideMessageAfterDelay();
    }

    private void showErrorMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setTextFill(javafx.scene.paint.Color.RED);
        messageLabel.setStyle("-fx-background-color: #f8d7da; -fx-text-fill: #721c24; -fx-background-radius: 5;");
        messageLabel.setVisible(true);
        hideMessageAfterDelay();
    }

    private void hideMessageAfterDelay() {
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                javafx.application.Platform.runLater(() -> messageLabel.setVisible(false));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}