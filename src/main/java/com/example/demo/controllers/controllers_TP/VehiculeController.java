package com.example.demo.controllers.controllers_TP;

import com.example.demo.enums.enums_TP.TypeVehicule;
import com.example.demo.models.models_TP.Ligne;
import com.example.demo.models.models_TP.Vehicule;
import com.example.demo.services.services_TP.LigneService;
import com.example.demo.services.services_TP.VehiculeService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;

public class VehiculeController {

    @FXML private ComboBox<TypeVehicule> typeField;
    @FXML private Label typeErrorLabel;
    @FXML private ComboBox<Ligne> ligneField;
    @FXML private Label ligneErrorLabel;
    @FXML private TextField vipPassengersField;
    @FXML private Label vipErrorLabel;
    @FXML private TextField premiumPassengersField;
    @FXML private Label premiumErrorLabel;
    @FXML private TextField economyPassengersField;
    @FXML private Label economyErrorLabel;
    @FXML private TextField vipAvailableField;
    @FXML private Label vipAvailableErrorLabel;
    @FXML private TextField premiumAvailableField;
    @FXML private Label premiumAvailableErrorLabel;
    @FXML private TextField economyAvailableField;
    @FXML private Label economyAvailableErrorLabel;
    @FXML private Label successLabel;
    @FXML private Button saveButton;
    @FXML private Button previousButton;

    private VehiculeService vehiculeService = new VehiculeService();
    private LigneService ligneService = new LigneService();

    @FXML
    public void initialize() {
        // Populate Type ComboBox
        typeField.getItems().setAll(TypeVehicule.values());

        // Populate Line ComboBox and set display to show only name
        ligneField.getItems().setAll(ligneService.getAll());
        ligneField.setConverter(new StringConverter<Ligne>() {
            @Override
            public String toString(Ligne ligne) {
                return ligne != null ? ligne.getName() : "";
            }

            @Override
            public Ligne fromString(String string) {
                return null;
            }
        });

        // Add real-time validation listeners
        typeField.valueProperty().addListener((obs, oldVal, newVal) -> {
            validateType();
            checkFormValidity();
        });
        ligneField.valueProperty().addListener((obs, oldVal, newVal) -> {
            validateLigne();
            checkFormValidity();
        });
        vipPassengersField.textProperty().addListener((obs, oldVal, newVal) -> {
            validateVipPassengers();
            checkFormValidity();
        });
        premiumPassengersField.textProperty().addListener((obs, oldVal, newVal) -> {
            validatePremiumPassengers();
            checkFormValidity();
        });
        economyPassengersField.textProperty().addListener((obs, oldVal, newVal) -> {
            validateEconomyPassengers();
            checkFormValidity();
        });
        vipAvailableField.textProperty().addListener((obs, oldVal, newVal) -> {
            validateVipAvailable();
            checkFormValidity();
        });
        premiumAvailableField.textProperty().addListener((obs, oldVal, newVal) -> {
            validatePremiumAvailable();
            checkFormValidity();
        });
        economyAvailableField.textProperty().addListener((obs, oldVal, newVal) -> {
            validateEconomyAvailable();
            checkFormValidity();
        });

        // Initially disable save button
        saveButton.setDisable(true);
    }

    @FXML
    public void saveVehicule() {
        if (validateForm()) {
            try {
                Vehicule vehicule = new Vehicule();
                vehicule.setType(typeField.getValue());
                vehicule.setLigne(ligneField.getValue());
                vehicule.setNbrMaxPassagersVIP(Integer.parseInt(vipPassengersField.getText()));
                vehicule.setNbrMaxPassagersPremium(Integer.parseInt(premiumPassengersField.getText()));
                vehicule.setNbrMaxPassagersEconomy(Integer.parseInt(economyPassengersField.getText()));
                vehicule.setPlacesDisponiblesVIP(Integer.parseInt(vipAvailableField.getText()));
                vehicule.setPlacesDisponiblesPremium(Integer.parseInt(premiumAvailableField.getText()));
                vehicule.setPlacesDisponiblesEconomy(Integer.parseInt(economyAvailableField.getText()));

                vehiculeService.add(vehicule);

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
                showErrorAlert("Entrée invalide", "Veuillez entrer des nombres valides pour les passagers.");
            } catch (Exception e) {
                showErrorAlert("Erreur", "Erreur lors de l'enregistrement: " + e.getMessage());
            }
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
            e.printStackTrace();
        }
    }

    private boolean validateForm() {
        return validateType() && validateLigne() && validateVipPassengers()
                && validatePremiumPassengers() && validateEconomyPassengers()
                && validateVipAvailable() && validatePremiumAvailable()
                && validateEconomyAvailable();
    }

    private boolean validateType() {
        boolean isValid = typeField.getValue() != null;
        typeErrorLabel.setVisible(!isValid);
        return isValid;
    }

    private boolean validateLigne() {
        boolean isValid = ligneField.getValue() != null;
        ligneErrorLabel.setVisible(!isValid);
        return isValid;
    }

    private boolean validateVipPassengers() {
        return validateIntegerField(vipPassengersField, vipErrorLabel);
    }

    private boolean validatePremiumPassengers() {
        return validateIntegerField(premiumPassengersField, premiumErrorLabel);
    }

    private boolean validateEconomyPassengers() {
        return validateIntegerField(economyPassengersField, economyErrorLabel);
    }

    private boolean validateVipAvailable() {
        return validateIntegerField(vipAvailableField, vipAvailableErrorLabel);
    }

    private boolean validatePremiumAvailable() {
        return validateIntegerField(premiumAvailableField, premiumAvailableErrorLabel);
    }

    private boolean validateEconomyAvailable() {
        return validateIntegerField(economyAvailableField, economyAvailableErrorLabel);
    }

    private boolean validateIntegerField(TextField field, Label errorLabel) {
        String text = field.getText().trim();
        if (text.isEmpty()) {
            errorLabel.setText("Champ obligatoire");
            errorLabel.setVisible(true);
            return false;
        }
        try {
            Integer.parseInt(text);
            errorLabel.setVisible(false);
            return true;
        } catch (NumberFormatException e) {
            errorLabel.setText("Valeur invalide");
            errorLabel.setVisible(true);
            return false;
        }
    }

    private void checkFormValidity() {
        saveButton.setDisable(!validateForm());
    }

    private void clearForm() {
        typeField.getSelectionModel().clearSelection();
        ligneField.getSelectionModel().clearSelection();
        vipPassengersField.clear();
        premiumPassengersField.clear();
        economyPassengersField.clear();
        vipAvailableField.clear();
        premiumAvailableField.clear();
        economyAvailableField.clear();
    }


    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}