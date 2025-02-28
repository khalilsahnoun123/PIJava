package com.example.demo.controllers.controllers_TP;

import com.example.demo.models.models_TP.Ligne;
import com.example.demo.services.services_TP.LigneService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LigneManagementController {

    @FXML private TableView<Ligne> ligneTable;
    @FXML private TableColumn<Ligne, Integer> idColumn;
    @FXML private TableColumn<Ligne, String> nameColumn;
    @FXML private TableColumn<Ligne, String> regionColumn;
    @FXML private TableColumn<Ligne, Integer> prixVIPColumn;
    @FXML private TableColumn<Ligne, Integer> prixPremiumColumn;
    @FXML private TableColumn<Ligne, Integer> prixEconoColumn;
    @FXML private Button addButton;
    @FXML private Button modifyButton;
    @FXML private Button deleteButton;
    @FXML private Button previousButton;
    @FXML private Label messageLabel;

    private LigneService ligneService = new LigneService();
    private ObservableList<Ligne> ligneList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set up table columns
        idColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        nameColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getName() != null ? cellData.getValue().getName() : "N/A"));
        regionColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getRegion() != null ? cellData.getValue().getRegion().toString() : "N/A"));
        prixVIPColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getPrixVIP().intValue()).asObject());
        prixPremiumColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getPrixPREMIUM().intValue()).asObject());
        prixEconoColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getPrixECONIMIC().intValue()).asObject());

        // Load data into table
        loadLigneData();

        // Enable buttons only when a row is selected
        ligneTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            modifyButton.setDisable(newSelection == null);
            deleteButton.setDisable(newSelection == null);
        });

        // Initially disable modify and delete buttons
        modifyButton.setDisable(true);
        deleteButton.setDisable(true);

        // Style buttons including the new previous button
        styleButtons();
    }

    private void loadLigneData() {
        ligneList.clear();
        ligneList.addAll(ligneService.getAll());
        ligneTable.setItems(ligneList);
    }

    private void styleButtons() {
        // Add hover effects for all buttons
        previousButton.setOnMouseEntered(e -> previousButton.setStyle(
                "-fx-background-color: #757575; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 4;"));
        previousButton.setOnMouseExited(e -> previousButton.setStyle(
                "-fx-background-color: #9E9E9E; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 4;"));

        addButton.setOnMouseEntered(e -> addButton.setStyle(
                "-fx-background-color: #45a049; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 4;"));
        addButton.setOnMouseExited(e -> addButton.setStyle(
                "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 4;"));

        modifyButton.setOnMouseEntered(e -> modifyButton.setStyle(
                "-fx-background-color: #1976D2; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 4;"));
        modifyButton.setOnMouseExited(e -> modifyButton.setStyle(
                "-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 4;"));

        deleteButton.setOnMouseEntered(e -> deleteButton.setStyle(
                "-fx-background-color: #D32F2F; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 4;"));
        deleteButton.setOnMouseExited(e -> deleteButton.setStyle(
                "-fx-background-color: #F44336; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 4;"));
    }

    @FXML
    private void goToAddLigne() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Ressource-TP/AddLigne.fxml"));
            Stage stage = (Stage) addButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter une Ligne");
        } catch (IOException e) {
            showErrorMessage("Erreur lors du chargement de la page d'ajout.");
            e.printStackTrace();
        }
    }

    @FXML
    private void goToModifyLigne() {
        Ligne selectedLigne = ligneTable.getSelectionModel().getSelectedItem();
        if (selectedLigne != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ressource-TP/ModifyLigne.fxml"));
                Parent root = loader.load();
                ModifyLigneController controller = loader.getController();
                controller.setLigneToModify(selectedLigne);

                Stage stage = (Stage) modifyButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier une Ligne");
            } catch (IOException e) {
                showErrorMessage("Erreur lors du chargement de la page de modification.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void deleteLigne() {
        Ligne selectedLigne = ligneTable.getSelectionModel().getSelectedItem();
        if (selectedLigne != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmer la suppression");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Voulez-vous vraiment supprimer la ligne " + selectedLigne.getName() + " ?");
            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        ligneService.delete(selectedLigne.getId());
                        loadLigneData();
                        showSuccessMessage("Ligne supprimée avec succès !");
                    } catch (Exception e) {
                        showErrorMessage("Erreur lors de la suppression : " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @FXML
    private void handlePrevious() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Ressource-TP/Accueil-TP.fxml"));
            Stage stage = (Stage) previousButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Transportation Management Dashboard");
        } catch (IOException e) {
            showErrorMessage("Erreur lors du retour à la page d'accueil : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showSuccessMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: green; -fx-font-size: 14px;");
        messageLabel.setVisible(true);
        fadeOutMessage();
    }

    private void showErrorMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
        messageLabel.setVisible(true);
        fadeOutMessage();
    }

    private void fadeOutMessage() {
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                messageLabel.setVisible(false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}