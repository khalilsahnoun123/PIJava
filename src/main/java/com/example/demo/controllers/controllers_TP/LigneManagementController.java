package com.example.demo.controllers.controllers_TP;

import com.example.demo.models.models_TP.Ligne;
import com.example.demo.services.services_TP.LigneService;
import javafx.application.Platform;
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

public class LigneManagementController {

    @FXML private GridPane gridPane;
    @FXML private Label messageLabel;
    @FXML private Button addButton;
    @FXML private Button previousButton;

    private List<Ligne> lignes;
    private LigneService ligneService = new LigneService();

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

        ColumnConstraints[] columns = {
                createColumn(10), createColumn(25), createColumn(15),
                createColumn(15), createColumn(15), createColumn(15), createColumn(10)
        };

        gridPane.getColumnConstraints().addAll(columns);
        addHeaderRow();
    }

    private ColumnConstraints createColumn(double percentage) {
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(percentage);
        return col;
    }

    private void addHeaderRow() {
        String[] headers = {"ID", "NOM", "RÉGION", "PRIX VIP", "PRIX PREMIUM", "PRIX ÉCONOMIQUE", "ACTIONS"};
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
        lignes = ligneService.getAll(); // Ensure this retrieves all data
        if (lignes != null && !lignes.isEmpty()) {
            refreshGrid();
        } else {
            showErrorMessage("No data found in the database.");
        }
    }
    private void refreshGrid() {
        Platform.runLater(() -> {
            // Clear existing rows (keep header row)
            gridPane.getChildren().removeIf(node ->
                    GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0
            );

            // Add rows starting from index 1 (index 0 is the header)
            int rowIndex = 1;
            for (Ligne ligne : lignes) {
                addLigneRow(ligne, rowIndex);
                rowIndex++;
            }
        });
    }

    private void addLigneRow(Ligne ligne, int rowIndex) {
        // ID
        Label idLabel = new Label(String.valueOf(ligne.getId()));
        idLabel.getStyleClass().add("grid-cell");
        gridPane.add(idLabel, 0, rowIndex);

        // Name
        Label nameLabel = new Label(ligne.getName());
        nameLabel.getStyleClass().add("grid-cell");
        gridPane.add(nameLabel, 1, rowIndex);

        // Region
        Label regionLabel = new Label(ligne.getRegion() != null ? ligne.getRegion().toString() : "N/A");
        regionLabel.getStyleClass().add("grid-cell");
        gridPane.add(regionLabel, 2, rowIndex);

        // Prix VIP
        Label prixVIPLabel = new Label(String.valueOf(ligne.getPrixVIP()));
        prixVIPLabel.getStyleClass().add("grid-cell");
        gridPane.add(prixVIPLabel, 3, rowIndex);

        // Prix Premium
        Label prixPremiumLabel = new Label(String.valueOf(ligne.getPrixPREMIUM()));
        prixPremiumLabel.getStyleClass().add("grid-cell");
        gridPane.add(prixPremiumLabel, 4, rowIndex);

        // Prix Economique
        Label prixEconoLabel = new Label(String.valueOf(ligne.getPrixECONIMIC()));
        prixEconoLabel.getStyleClass().add("grid-cell");
        gridPane.add(prixEconoLabel, 5, rowIndex);

        // Action Buttons
        HBox actionBox = new HBox(10);
        Button editButton = new Button("Modifier");
        Button deleteButton = new Button("Supprimer");

        editButton.getStyleClass().add("grid-action-button");
        deleteButton.getStyleClass().add("grid-action-button");

        editButton.setOnAction(e -> handleEdit(ligne));
        deleteButton.setOnAction(e -> handleDelete(ligne));

        actionBox.getChildren().addAll(editButton, deleteButton);
        gridPane.add(actionBox, 6, rowIndex);

        // Apply hover effect to the entire row
        applyRowHoverEffect(rowIndex);
    }
    private void addGridCell(String value, int columnIndex, int rowIndex) {
        Label label = new Label(value);
        label.getStyleClass().add("grid-cell");
        gridPane.add(label, columnIndex, rowIndex);
    }

    private void applyRowHoverEffect(int rowIndex) {
        gridPane.getChildren().forEach(node -> {
            if (GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == rowIndex) {
                node.setOnMouseEntered(e -> {
                    gridPane.getChildren()
                            .filtered(n -> GridPane.getRowIndex(n) != null && GridPane.getRowIndex(n) == rowIndex)
                            .forEach(n -> n.setStyle("-fx-background-color: #f8f9fa;"));
                });
                node.setOnMouseExited(e -> {
                    gridPane.getChildren()
                            .filtered(n -> GridPane.getRowIndex(n) != null && GridPane.getRowIndex(n) == rowIndex)
                            .forEach(n -> n.setStyle(""));
                });
            }
        });
    }

    private void handleEdit(Ligne ligne) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ressource-TP/ModifyLigne.fxml"));
            Parent root = loader.load();
            ModifyLigneController controller = loader.getController();
            controller.setLigneToModify(ligne);

            Stage stage = (Stage) gridPane.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showErrorMessage("Erreur: " + e.getMessage());
        }
    }

    private void handleDelete(Ligne ligne) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer " + ligne.getName() + "?");
        alert.setContentText("Cette action est irréversible.");

        if (alert.showAndWait().get() == ButtonType.OK) {
            ligneService.delete(ligne.getId());
            showSuccessMessage(ligne.getName() + " supprimée avec succès!");
            loadData();
        }
    }

    @FXML
    private void goToAddLigne() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Ressource-TP/AddLigne.fxml"));
            Stage stage = (Stage) addButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter une Ligne");
        } catch (IOException e) {
            showErrorMessage("Erreur: " + e.getMessage());
        }
    }

    @FXML
    private void handlePrevious() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/resource-Velo/DashboardAdmin.fxml"));
            Stage stage = (Stage) previousButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showErrorMessage("Erreur: " + e.getMessage());
        }
    }

    private void showSuccessMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-background-color: #d4edda; -fx-text-fill: #155724; -fx-background-radius: 5;");
        messageLabel.setVisible(true);
        hideMessageAfterDelay();
    }

    private void showErrorMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-background-color: #f8d7da; -fx-text-fill: #721c24; -fx-background-radius: 5;");
        messageLabel.setVisible(true);
        hideMessageAfterDelay();
    }

    private void hideMessageAfterDelay() {
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                Platform.runLater(() -> messageLabel.setVisible(false));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}