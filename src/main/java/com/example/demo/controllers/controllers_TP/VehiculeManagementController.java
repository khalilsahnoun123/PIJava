package com.example.demo.controllers.controllers_TP;

import com.example.demo.models.models_TP.Vehicule;
import com.example.demo.services.services_TP.VehiculeService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class VehiculeManagementController {

    @FXML
    private TableView<Vehicule> vehiculeTable;
    @FXML
    private TableColumn<Vehicule, Integer> idColumn;
    @FXML
    private TableColumn<Vehicule, String> typeColumn;
    @FXML
    private TableColumn<Vehicule, String> ligneColumn;
    @FXML
    private TableColumn<Vehicule, Integer> vipMaxColumn;
    @FXML
    private TableColumn<Vehicule, Integer> premiumMaxColumn;
    @FXML
    private TableColumn<Vehicule, Integer> economyMaxColumn;
    @FXML
    private TableColumn<Vehicule, Integer> vipAvailableColumn;
    @FXML
    private TableColumn<Vehicule, Integer> premiumAvailableColumn;
    @FXML
    private TableColumn<Vehicule, Integer> economyAvailableColumn;
    @FXML
    private TableColumn<Vehicule, Void> actionsColumn;
    @FXML private Button previousButton;

    @FXML
    private TextField searchField;
    @FXML
    private Label messageLabel;
    @FXML
    private Button addButton;
    @FXML
    private Button prevButton;
    @FXML
    private Button nextButton;
    @FXML
    private Label pageLabel;

    private VehiculeService vehiculeService = new VehiculeService();
    private ObservableList<Vehicule> vehiculeList = FXCollections.observableArrayList();
    private ObservableList<Vehicule> filteredList = FXCollections.observableArrayList();
    private int currentPage = 0;
    private final int ITEMS_PER_PAGE = 10;

    @FXML
    public void initialize() {
        // Configure columns with correct property names
        idColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        typeColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getType() != null ? cellData.getValue().getType().toString() : "N/A"));
        ligneColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getLigne() != null ? cellData.getValue().getLigne().getName() : "N/A"));
        vipMaxColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getNbrMaxPassagersVIP()).asObject());
        premiumMaxColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getNbrMaxPassagersPremium()).asObject());
        economyMaxColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getNbrMaxPassagersEconomy()).asObject());
        vipAvailableColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getPlacesDisponiblesVIP()).asObject());
        premiumAvailableColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getPlacesDisponiblesPremium()).asObject());
        economyAvailableColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getPlacesDisponiblesEconomy()).asObject());
        // Action column with buttons
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button modifyButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");

            {
                modifyButton.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; " +
                        "-fx-font-size: 12px; -fx-padding: 5 10; -fx-background-radius: 5;");
                deleteButton.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; " +
                        "-fx-font-size: 12px; -fx-padding: 5 10; -fx-background-radius: 5;");
                modifyButton.setOnAction(event -> {
                    Vehicule vehicule = getTableView().getItems().get(getIndex());
                    handleModify(vehicule);
                });

                deleteButton.setOnAction(event -> {
                    Vehicule vehicule = getTableView().getItems().get(getIndex());
                    handleDelete(vehicule);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(10, modifyButton, deleteButton);
                    hbox.setAlignment(javafx.geometry.Pos.CENTER);
                    setGraphic(hbox);
                }
            }
        });

        // Load initial data
        loadVehicules();
        updateTableView();
    }

    private void loadVehicules() {
        vehiculeList.setAll(vehiculeService.getAll());
        filteredList.setAll(vehiculeList);
        // Debug output to verify data
        vehiculeList.forEach(v -> System.out.println("Vehicule: " + v.getId() + ", Type: " + v.getType() +
                ", VIP Max: " + v.getNbrMaxPassagersVIP() + ", Ligne: " + (v.getLigne() != null ? v.getLigne().getName() : "null")));
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
                loadVehicules();
                updateTableView();
                showSuccessMessage("Véhicule supprimé avec succès!");
            } catch (Exception e) {
                showErrorMessage("Erreur lors de la suppression: " + e.getMessage());
            }
        }
    }

    @FXML
    private void filterVehicules() {
        String searchText = searchField.getText().toLowerCase();
        filteredList.clear();
        if (searchText.isEmpty()) {
            filteredList.setAll(vehiculeList);
        } else {
            for (Vehicule v : vehiculeList) {
                if (String.valueOf(v.getId()).contains(searchText) ||
                        (v.getType() != null && v.getType().toString().toLowerCase().contains(searchText)) ||
                        (v.getLigne() != null && v.getLigne().getName().toLowerCase().contains(searchText))) {
                    filteredList.add(v);
                }
            }
        }
        currentPage = 0;
        updateTableView();
    }

    @FXML
    private void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            updateTableView();
        }
    }

    @FXML
    private void nextPage() {
        int totalPages = (int) Math.ceil((double) filteredList.size() / ITEMS_PER_PAGE);
        if (currentPage < totalPages - 1) {
            currentPage++;
            updateTableView();
        }
    }

    private void updateTableView() {
        int fromIndex = currentPage * ITEMS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, filteredList.size());
        ObservableList<Vehicule> pageList = FXCollections.observableArrayList(
                filteredList.subList(fromIndex, toIndex));
        vehiculeTable.setItems(pageList);

        int totalPages = (int) Math.ceil((double) filteredList.size() / ITEMS_PER_PAGE);
        pageLabel.setText(String.format("Page %d / %d", currentPage + 1, Math.max(1, totalPages)));

        prevButton.setDisable(currentPage == 0);
        nextButton.setDisable(toIndex >= filteredList.size());
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