package com.example.demo.controllers.controllers_TP;

import com.example.demo.enums.enums_TP.TypeVehicule;
import com.example.demo.models.models_TP.Ligne;
import com.example.demo.models.models_TP.Vehicule;
import com.example.demo.services.services_TP.LigneService;
import com.example.demo.services.services_TP.VehiculeService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class UpdateVehiculeController {

    @FXML private TextField idField;
    @FXML private ComboBox<TypeVehicule> typeComboBox;
    @FXML private ComboBox<Ligne> ligneComboBox;
    @FXML private TextField vipMaxField;
    @FXML private TextField premiumMaxField;
    @FXML private TextField economyMaxField;
    @FXML private TextField vipAvailableField;
    @FXML private TextField premiumAvailableField;
    @FXML private TextField economyAvailableField;
    @FXML private Button saveButton;
    @FXML private Button previousButton;
    @FXML private Label messageLabel;

    private VehiculeService vehiculeService = new VehiculeService();
    private LigneService ligneService = new LigneService();
    private Vehicule vehiculeToUpdate;

    @FXML
    public void initialize() {
        // Populate ComboBoxes
        typeComboBox.setItems(FXCollections.observableArrayList(TypeVehicule.values()));
        ligneComboBox.setItems(FXCollections.observableArrayList(ligneService.getAll()));

        // Custom cell factory for ligneComboBox
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

        // Add input validation for TextFields
        addNumericValidation(vipMaxField);
        addNumericValidation(premiumMaxField);
        addNumericValidation(economyMaxField);
        addNumericValidation(vipAvailableField);
        addNumericValidation(premiumAvailableField);
        addNumericValidation(economyAvailableField);
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehiculeToUpdate = vehicule;
        idField.setText(String.valueOf(vehicule.getId()));
        typeComboBox.getSelectionModel().select(vehicule.getType());
        ligneComboBox.getSelectionModel().select(vehicule.getLigne());
        vipMaxField.setText(String.valueOf(vehicule.getNbrMaxPassagersVIP()));
        premiumMaxField.setText(String.valueOf(vehicule.getNbrMaxPassagersPremium()));
        economyMaxField.setText(String.valueOf(vehicule.getNbrMaxPassagersEconomy()));
        vipAvailableField.setText(String.valueOf(vehicule.getPlacesDisponiblesVIP()));
        premiumAvailableField.setText(String.valueOf(vehicule.getPlacesDisponiblesPremium()));
        economyAvailableField.setText(String.valueOf(vehicule.getPlacesDisponiblesEconomy()));
    }

    @FXML
    private void saveVehicule() {
        try {
            // Validate required fields
            if (typeComboBox.getValue() == null) {
                showErrorMessage("Veuillez sélectionner un type de véhicule.");
                return;
            }
            if (ligneComboBox.getValue() == null) {
                showErrorMessage("Veuillez sélectionner une ligne.");
                return;
            }

            // Validate numeric fields
            if (!validateNumericFields()) {
                return;
            }

            // Update vehicle object
            vehiculeToUpdate.setType(typeComboBox.getValue());
            vehiculeToUpdate.setLigne(ligneComboBox.getValue());
            vehiculeToUpdate.setNbrMaxPassagersVIP(Integer.parseInt(vipMaxField.getText()));
            vehiculeToUpdate.setNbrMaxPassagersPremium(Integer.parseInt(premiumMaxField.getText()));
            vehiculeToUpdate.setNbrMaxPassagersEconomy(Integer.parseInt(economyMaxField.getText()));
            vehiculeToUpdate.setPlacesDisponiblesVIP(Integer.parseInt(vipAvailableField.getText()));
            vehiculeToUpdate.setPlacesDisponiblesPremium(Integer.parseInt(premiumAvailableField.getText()));
            vehiculeToUpdate.setPlacesDisponiblesEconomy(Integer.parseInt(economyAvailableField.getText()));

            // Additional business logic validation
            if (!validatePassengerLogic()) {
                return;
            }

            vehiculeService.update(vehiculeToUpdate);
            showSuccessMessage("Véhicule mis à jour avec succès!");
            goBack();
        } catch (Exception e) {
            showErrorMessage("Erreur lors de la mise à jour: " + e.getMessage());
        }
    }

    @FXML
    private void goBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Ressource-TP/VehiculeManagement.fxml"));
            Stage stage = (Stage) previousButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestion des Véhicules");
        } catch (IOException e) {
            showErrorMessage("Erreur lors du retour: " + e.getMessage());
        }
    }

    // Input validation for numeric fields
    private void addNumericValidation(TextField field) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                field.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (newValue.isEmpty()) {
                field.setStyle("-fx-border-color: red;");
            } else {
                field.setStyle("-fx-border-color: lightgray; -fx-background-radius: 5;");
            }
        });
    }

    // Validate all numeric fields
    private boolean validateNumericFields() {
        TextField[] fields = {vipMaxField, premiumMaxField, economyMaxField,
                vipAvailableField, premiumAvailableField, economyAvailableField};

        for (TextField field : fields) {
            if (field.getText().isEmpty()) {
                showErrorMessage("Tous les champs numériques doivent être remplis.");
                field.setStyle("-fx-border-color: red;");
                return false;
            }
            try {
                int value = Integer.parseInt(field.getText());
                if (value < 0) {
                    showErrorMessage("Les nombres doivent être positifs.");
                    field.setStyle("-fx-border-color: red;");
                    return false;
                }
            } catch (NumberFormatException e) {
                showErrorMessage("Valeur numérique invalide dans " + field.getPromptText());
                field.setStyle("-fx-border-color: red;");
                return false;
            }
        }
        return true;
    }

    // Validate passenger logic (available <= max)
    private boolean validatePassengerLogic() {
        int vipMax = Integer.parseInt(vipMaxField.getText());
        int premiumMax = Integer.parseInt(premiumMaxField.getText());
        int ecoMax = Integer.parseInt(economyMaxField.getText());
        int vipAvail = Integer.parseInt(vipAvailableField.getText());
        int premiumAvail = Integer.parseInt(premiumAvailableField.getText());
        int ecoAvail = Integer.parseInt(economyAvailableField.getText());

        if (vipAvail > vipMax || premiumAvail > premiumMax || ecoAvail > ecoMax) {
            showErrorMessage("Les places disponibles ne peuvent pas dépasser les maximums.");
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