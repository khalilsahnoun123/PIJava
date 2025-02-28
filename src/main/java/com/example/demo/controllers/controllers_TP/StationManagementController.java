package com.example.demo.controllers.controllers_TP;

import com.example.demo.models.models_TP.Station;
import com.example.demo.services.services_TP.StationService;
import javafx.application.Platform;
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

public class StationManagementController {
    @FXML private GridPane gridPane;
    @FXML private Label messageLabel;
    @FXML private Button addButton;
    @FXML private Button previousButton;

    private List<Station> stations;
    private StationService stationService = new StationService();



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
        // Clear existing grid constraints
        gridPane.getColumnConstraints().clear();

        // Create column constraints
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(10);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(25);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(35);
        ColumnConstraints col4 = new ColumnConstraints();
        col4.setPercentWidth(20);
        ColumnConstraints col5 = new ColumnConstraints();
        col5.setPercentWidth(10);

        gridPane.getColumnConstraints().addAll(col1, col2, col3, col4, col5);

        // Add header row
        addHeaderRow();
    }

    private void addHeaderRow() {
        String[] headers = {"ID", "NOM", "ADRESSE", "LIGNE", "ACTIONS"};
        for (int i = 0; i < headers.length; i++) {
            Label header = new Label(headers[i]);
            header.getStyleClass().add("grid-header");
            gridPane.add(header, i, 0);
        }
    }
    private void styleComponents() {
        if (gridPane != null) { // Add null check
            gridPane.setHgap(15);
            gridPane.setVgap(10);
        }
    }


    private void loadData() {
        stations = stationService.getAll();
        refreshGrid();
    }

    private void refreshGrid() {
        Platform.runLater(() -> {
            gridPane.getChildren().removeIf(node ->
                    GridPane.getRowIndex(node) != null &&
                            GridPane.getRowIndex(node) > 0
            );

            int rowIndex = 1;
            for (Station station : stations) {
                addStationRow(station, rowIndex);
                rowIndex++;
            }
        });
    }

    private void addStationRow(Station station, int rowIndex) {
        // ID
        Label idLabel = new Label(String.valueOf(station.getId()));
        idLabel.getStyleClass().add("grid-cell");
        gridPane.add(idLabel, 0, rowIndex);

        // Nom
        Label nomLabel = new Label(station.getNom());
        nomLabel.getStyleClass().add("grid-cell");
        gridPane.add(nomLabel, 1, rowIndex);

        // Adresse
        Label adresseLabel = new Label(station.getAdresse());
        adresseLabel.getStyleClass().add("grid-cell");
        gridPane.add(adresseLabel, 2, rowIndex);

        // Ligne
        Label ligneLabel = new Label(station.getLigne() != null ? station.getLigne().getName() : "N/A");
        ligneLabel.getStyleClass().add("grid-cell");
        gridPane.add(ligneLabel, 3, rowIndex);

        // Action Buttons
        HBox actionBox = new HBox(10);
        Button editButton = new Button("Modifier");
        Button deleteButton = new Button("Supprimer");

        editButton.getStyleClass().add("grid-action-button");
        deleteButton.getStyleClass().add("grid-action-button");

        editButton.setOnAction(e -> handleEdit(station));
        deleteButton.setOnAction(e -> handleDelete(station));

        actionBox.getChildren().addAll(editButton, deleteButton);
        gridPane.add(actionBox, 4, rowIndex);

        // Hover effect
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

    private void handleEdit(Station station) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ressource-TP/ModifyStation.fxml"));
            Parent root = loader.load();
            ModifyStationController controller = loader.getController();
            controller.setStation(station);
            Stage stage = (Stage) gridPane.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showErrorMessage("Erreur: " + e.getMessage());
        }
    }

    private void handleDelete(Station station) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer " + station.getNom() + "?");
        alert.setContentText("Cette action est irréversible.");

        if (alert.showAndWait().get() == ButtonType.OK) {
            stationService.delete(station.getId());

            showSuccessMessage(station.getNom() + " supprimée avec succès!");
            loadData();
        }
    }

    @FXML
    private void goToAddStation() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Ressource-TP/AddStation-TP.fxml"));
            Stage stage = (Stage) addButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter une Station");
        } catch (IOException e) {
            showErrorMessage("Erreur lors de l'ouverture de l'ajout: " + e.getMessage());
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