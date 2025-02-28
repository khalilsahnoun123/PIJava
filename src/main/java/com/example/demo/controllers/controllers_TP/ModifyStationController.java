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

public class ModifyStationController {

    @FXML private TextField idField;
    @FXML private TextField nomField;
    @FXML private TextField adresseField;
    @FXML private ComboBox<Ligne> ligneComboBox;

    @FXML private Button previousButton;
    @FXML private Label messageLabel;

    private StationService stationService = new StationService();
    private LigneService ligneService = new LigneService();
    private Station stationToModify;

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

        // Add real-time input validation
        addTextValidation(nomField, "Le nom de la station est requis.");
        addTextValidation(adresseField, "L'adresse est requise.");

        // Validate ComboBox selection
        ligneComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) {
                ligneComboBox.setStyle("-fx-border-color: red;");
            } else {
                ligneComboBox.setStyle("-fx-border-color: lightgray;");
            }
        });
    }

    public void setStation(Station station) {
        this.stationToModify = station;
        idField.setText(String.valueOf(station.getId()));
        nomField.setText(station.getNom());
        adresseField.setText(station.getAdresse());
        ligneComboBox.getSelectionModel().select(station.getLigne());
    }

    @FXML
    private void saveStation() {
        try {
            if (!validateFields()) {
                return;
            }

            stationToModify.setNom(nomField.getText().trim());
            stationToModify.setAdresse(adresseField.getText().trim());
            stationToModify.setLigne(ligneComboBox.getValue());
            System.out.println(stationToModify.toString());
            stationService.update(stationToModify);
            showSuccessMessage("Station modifiée avec succès!");
            goBack();
        } catch (Exception e) {
            showErrorMessage("Erreur lors de la modification: " + e.getMessage());
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

    private void addTextValidation(TextField field, String errorMessage) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isEmpty()) {
                field.setStyle("-fx-border-color: red;");
            } else if (newValue.length() > 100) { // Example max length
                field.setText(newValue.substring(0, 100));
                field.setStyle("-fx-border-color: orange;");
                showErrorMessage("Maximum 100 caractères pour " + field.getPromptText());
            } else {
                field.setStyle("-fx-border-color: lightgray; -fx-background-radius: 5;");
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
        messageLabel.setVisible(true);
        hideMessageAfterDelay();
    }

    private void showErrorMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setTextFill(javafx.scene.paint.Color.RED);
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