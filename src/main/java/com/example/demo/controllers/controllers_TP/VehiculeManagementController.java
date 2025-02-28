package com.example.demo.controllers.controllers_TP;

import com.example.demo.models.models_TP.Vehicule;
import com.example.demo.services.services_TP.VehiculeService;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class VehiculeManagementController {

    @FXML private GridPane gridPane;
    @FXML private Label messageLabel;

    @FXML private Button previousButton;



    private List<Vehicule> vehicules;

    private VehiculeService vehiculeService = new VehiculeService();



    @FXML
    public void initialize() {
        if (gridPane == null) {
            gridPane = new GridPane();
            gridPane.getStyleClass().add("grid-pane");
        }
        setupGrid();
        styleComponents();
        loadData();
    }

    private void setupGrid() {
        gridPane.getColumnConstraints().clear();

        // Column constraints (matches FXML percentages)
        double[] percentages = {8, 15, 15, 10, 10, 10, 10, 10, 10, 10};
        for (double percent : percentages) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(percent);
            gridPane.getColumnConstraints().add(col);
        }

        addHeaderRow();
    }

    private void addHeaderRow() {
        String[] headers = {"ID", "Type", "Ligne", "VIP Max", "Premium Max", "Éco Max",
                "VIP Dispo", "Premium Dispo", "Éco Dispo", "Actions"};
        for (int i = 0; i < headers.length; i++) {
            Label header = new Label(headers[i]);
            header.getStyleClass().add("grid-header");
            gridPane.add(header, i, 0);
        }
    }

    private void styleComponents() {
        if (gridPane != null) {
            gridPane.setHgap(10);
            gridPane.setVgap(8);
        }
    }

    private void loadData() {
        vehicules = vehiculeService.getAll();
        // Debug: Check number of retrieved vehicles
        System.out.println("Number of vehicles retrieved: " + vehicules.size());
        if (vehicules == null || vehicules.isEmpty()) {
            showErrorMessage("No data found in the database.");
            return;
        }

        refreshGrid();
    }
    private void refreshGrid() {
        Platform.runLater(() -> {
            // Clear existing rows (keep header row)
            gridPane.getChildren().removeIf(node ->
                    GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0
            );

            int rowIndex = 1;
            for (Vehicule vehicule : vehicules)
            {System.out.println(vehicule);
                addVehiculeRow(vehicule, rowIndex);
                rowIndex++;
            }
        });
    }

    private void addVehiculeRow(Vehicule vehicule, int rowIndex) {
        addGridCell(String.valueOf(vehicule.getId()), 0, rowIndex);
        addGridCell(vehicule.getType() != null ? vehicule.getType().toString() : "N/A", 1, rowIndex);
        addGridCell(vehicule.getLigne() != null ? vehicule.getLigne().getName() : "N/A", 2, rowIndex);
        addGridCell(String.valueOf(vehicule.getNbrMaxPassagersVIP()), 3, rowIndex);
        addGridCell(String.valueOf(vehicule.getNbrMaxPassagersPremium()), 4, rowIndex);
        addGridCell(String.valueOf(vehicule.getNbrMaxPassagersEconomy()), 5, rowIndex);
        addGridCell(String.valueOf(vehicule.getPlacesDisponiblesVIP()), 6, rowIndex);
        addGridCell(String.valueOf(vehicule.getPlacesDisponiblesPremium()), 7, rowIndex);
        addGridCell(String.valueOf(vehicule.getPlacesDisponiblesEconomy()), 8, rowIndex);

        // Action Buttons
        HBox actionBox = new HBox(10);
        Button editButton = new Button("Modifier");
        Button deleteButton = new Button("Supprimer");

        editButton.getStyleClass().add("grid-action-button");
        deleteButton.getStyleClass().add("grid-action-button");

        editButton.setOnAction(e -> handleModify(vehicule));
        deleteButton.setOnAction(e -> handleDelete(vehicule));

        actionBox.getChildren().addAll(editButton, deleteButton);
        gridPane.add(actionBox, 9, rowIndex);

        applyRowHoverEffect(rowIndex);
    }

    private void addGridCell(String value, int columnIndex, int rowIndex) {
        Label label = new Label(value);
        label.getStyleClass().add("grid-cell");
        gridPane.add(label, columnIndex, rowIndex);
    }


    private void applyRowHoverEffect(Integer rowIndex) {
        gridPane.getChildren().forEach(node -> {
            if (GridPane.getRowIndex(node) == rowIndex) {
                node.setOnMouseEntered(e -> gridPane.getChildren()
                        .filtered(n -> GridPane.getRowIndex(n) == rowIndex)
                        .forEach(n -> n.setStyle("-fx-background-color: #f8f9fa;")));
                node.setOnMouseExited(e -> gridPane.getChildren()
                        .filtered(n -> GridPane.getRowIndex(n) == rowIndex)
                        .forEach(n -> n.setStyle("")));
            }
        });
    }



    @FXML
    private void goToAddVehicule() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Ressource-TP/AddVehicule-TP.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un Véhicule");
            stage.show();
        } catch (IOException e) {
            showErrorMessage("Erreur lors du chargement: " + e.getMessage());
        }
    }

    private void handleModify(Vehicule vehicule) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ressource-TP/ModifyVehicule.fxml"));
            Parent root = loader.load();
            UpdateVehiculeController controller = loader.getController();
            controller.setVehicule(vehicule); // Pass the selected vehicle to the update controller

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier le Véhicule #" + vehicule.getId());
            stage.show();
        } catch (IOException e) {
            showErrorMessage("Erreur lors de la modification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleDelete(Vehicule vehicule) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Supprimer le véhicule #" + vehicule.getId());
        confirm.setContentText("Êtes-vous sûr de vouloir supprimer ce véhicule?");

        if (confirm.showAndWait().get() == ButtonType.OK) {
            try {
                vehiculeService.delete(vehicule.getId());
                loadData();

                showSuccessMessage("Véhicule supprimé avec succès!");
            } catch (Exception e) {
                showErrorMessage("Erreur lors de la suppression: " + e.getMessage());
            }
        }
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
    @FXML
    public void goBack(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Ressource-TP/Accueil-TP.fxml"));
            Stage stage = (Stage) previousButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestion ");
        } catch (IOException e) {
            showErrorMessage("Erreur lors du retour: " + e.getMessage());
        }
    }
}