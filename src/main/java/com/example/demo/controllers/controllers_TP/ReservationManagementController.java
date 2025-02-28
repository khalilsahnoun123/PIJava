package com.example.demo.controllers.controllers_TP;

import com.example.demo.enums.enums_TP.ReservationStatus;
import com.example.demo.enums.enums_TP.TicketCategory;
import com.example.demo.models.models_TP.Reservation;
import com.example.demo.services.services_TP.ReservationService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class ReservationManagementController {

    @FXML private TableView<Reservation> reservationTable;
    @FXML private TableColumn<Reservation, Integer> idColumn;
    @FXML private TableColumn<Reservation, String> reservationDateColumn;
    @FXML private TableColumn<Reservation, String> travelDateColumn;
    @FXML private TableColumn<Reservation, Integer> seatsColumn;
    @FXML private TableColumn<Reservation, TicketCategory> ticketCategoryColumn;
    @FXML private TableColumn<Reservation, ReservationStatus> statusColumn;
    @FXML private TableColumn<Reservation, Double> totalPriceColumn;
    @FXML private TableColumn<Reservation, String> vehicleColumn;
    @FXML private TableColumn<Reservation, String> departStationColumn;
    @FXML private TableColumn<Reservation, String> finStationColumn;
    @FXML private Button addButton;
    @FXML private Button modifyButton;
    @FXML private Button deleteButton;
    @FXML private Button refreshButton;
    @FXML private Button previousButton; // Added
    @FXML private Label messageLabel;

    private ReservationService reservationService = new ReservationService();
    private ObservableList<Reservation> reservationList = FXCollections.observableArrayList();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML
    public void initialize() {
        // Set up table columns
        idColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getReservationId()).asObject());

        reservationDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getReservationDate().format(DATE_FORMATTER)));

        travelDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTravelDate().format(DATE_FORMATTER)));

        seatsColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getNumberOfSeats()).asObject());

        ticketCategoryColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getTicketCategory()));

        statusColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getStatus()));

        totalPriceColumn.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getTotalPrice()).asObject());

        vehicleColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getVehicule() != null ?
                        String.valueOf(cellData.getValue().getVehicule().getId()) : "N/A"));

        departStationColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDepartStation() != null ?
                        String.valueOf(cellData.getValue().getDepartStation().getId()) : "N/A"));

        finStationColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFinStation() != null ?
                        String.valueOf(cellData.getValue().getFinStation().getId()) : "N/A"));

        // Load initial data
        loadReservationData();

        // Enable modify/delete buttons only when a row is selected
        reservationTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            modifyButton.setDisable(newSelection == null);
            deleteButton.setDisable(newSelection == null);
        });

        // Initially disable modify and delete buttons
        modifyButton.setDisable(true);
        deleteButton.setDisable(true);

        // Style buttons
        styleButtons();
    }

    private void loadReservationData() {
        reservationList.clear();
        reservationList.addAll(reservationService.getAll());
        reservationTable.setItems(reservationList);
    }

    private void styleButtons() {
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        modifyButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        deleteButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");
        refreshButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white;");
        previousButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white;");

        // Hover effects
        previousButton.setOnMouseEntered(e -> previousButton.setStyle("-fx-background-color: #757575; -fx-text-fill: white;"));
        previousButton.setOnMouseExited(e -> previousButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white;"));
    }

    @FXML
    private void handleAddReservation() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Ressource-TP/AddReservation-TP.fxml"));
            Stage stage = (Stage) addButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Add New Reservation");
        } catch (IOException e) {
            showErrorMessage("Error loading add reservation page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleModifyReservation() {
        Reservation selectedReservation = reservationTable.getSelectionModel().getSelectedItem();
        if (selectedReservation != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ressource-TP/ModifyReservation.fxml"));
                Parent root = loader.load();
                ModifyReservationController controller = loader.getController();
                controller.setReservationToModify(selectedReservation);

                Stage stage = (Stage) modifyButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Modify Reservation");
            } catch (IOException e) {
                showErrorMessage("Error loading modify page: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleDeleteReservation() {
        Reservation selectedReservation = reservationTable.getSelectionModel().getSelectedItem();
        if (selectedReservation != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirm Deletion");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Are you sure you want to delete reservation #" +
                    selectedReservation.getReservationId() + "?");

            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        reservationService.delete(selectedReservation.getReservationId());
                        loadReservationData();
                        showSuccessMessage("Reservation deleted successfully!");
                    } catch (Exception e) {
                        showErrorMessage("Error deleting reservation: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @FXML
    private void handleRefresh() {
        loadReservationData();
        showSuccessMessage("Table refreshed successfully!");
    }

    @FXML
    private void handlePrevious() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Ressource-TP/Accueil-TP.fxml"));
            Stage stage = (Stage) previousButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Transportation Management Dashboard");
        } catch (IOException e) {
            showErrorMessage("Error returning to home: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showSuccessMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: green;");
        messageLabel.setVisible(true);
        fadeOutMessage();
    }

    private void showErrorMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: red;");
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