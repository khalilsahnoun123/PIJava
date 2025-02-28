package com.example.demo.controllers.controllers_TP;

import com.example.demo.enums.enums_TP.ReservationStatus;
import com.example.demo.enums.enums_TP.TicketCategory;
import com.example.demo.models.models_TP.Reservation;
import com.example.demo.models.models_TP.Station;
import com.example.demo.models.models_TP.Vehicule;
import com.example.demo.services.services_TP.ReservationService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class AddReservationController {

    @FXML private TextField numberOfSeatsField;
    @FXML private TextField totalPriceField;
    @FXML private ChoiceBox<TicketCategory> ticketCategoryChoice;
    @FXML private ChoiceBox<ReservationStatus> statusChoice;
    @FXML private ChoiceBox<Vehicule> vehicleChoice;
    @FXML private ChoiceBox<Station> departStationChoice;
    @FXML private ChoiceBox<Station> finStationChoice;
    @FXML private DatePicker travelDatePicker;
    @FXML private Button addButton;
    @FXML private Button previousButton;
    @FXML private Label messageLabel;

    private ReservationService reservationService = new ReservationService();

    @FXML
    public void initialize() {
        // Initialize choice boxes with enum values
        ticketCategoryChoice.getItems().addAll(TicketCategory.values());
        statusChoice.getItems().addAll(ReservationStatus.values());

        // Populate with sample data (replace with actual service calls)
        vehicleChoice.getItems().addAll(getVehiclesFromService());
        departStationChoice.getItems().addAll(getStationsFromService());
        finStationChoice.getItems().addAll(getStationsFromService());

        // Set default values
        statusChoice.setValue(ReservationStatus.CONFIRMED);
        ticketCategoryChoice.setValue(TicketCategory.ECONOMIC);

        // Add input validation
        setupInputValidation();

        // Style components
        styleComponents();
    }

    private void setupInputValidation() {
        // Number of seats validation (only positive integers)
        numberOfSeatsField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                numberOfSeatsField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        // Total price validation (numbers with decimals)
        totalPriceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                totalPriceField.setText(oldValue);
            }
        });
    }

    private void styleComponents() {
        // Add padding and spacing
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);

        // Style buttons
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        previousButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");

        // Add hover effects
        addButton.setOnMouseEntered(e -> addButton.setStyle("-fx-background-color: #45a049; -fx-text-fill: white;"));
        addButton.setOnMouseExited(e -> addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"));
        previousButton.setOnMouseEntered(e -> previousButton.setStyle("-fx-background-color: #1976D2; -fx-text-fill: white;"));
        previousButton.setOnMouseExited(e -> previousButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;"));
    }

    @FXML
    private void handleAddReservation() {
        if (!validateInputs()) {
            showErrorMessage("Please fill all required fields correctly");
            return;
        }

        try {
            Reservation reservation = Reservation.builder()
                    .reservationDate(LocalDateTime.now())
                    .travelDate(travelDatePicker.getValue().atStartOfDay())
                    .numberOfSeats(Integer.parseInt(numberOfSeatsField.getText()))
                    .ticketCategory(ticketCategoryChoice.getValue())
                    .status(statusChoice.getValue())
                    .totalPrice(Double.parseDouble(totalPriceField.getText()))
                    .vehicule(vehicleChoice.getValue())
                    .departStation(departStationChoice.getValue())
                    .finStation(finStationChoice.getValue())
                    .build();

            reservationService.add(reservation);
            showSuccessMessage("Reservation added successfully!");
            clearFields();
        } catch (Exception e) {
            showErrorMessage("Error adding reservation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePrevious() {
        try {
            Stage stage = (Stage) previousButton.getScene().getWindow();
            // Replace with your previous scene FXML
            stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/Ressource-TP/ReservationManagement.fxml")).load()));
            stage.setTitle("Reservation Management");
        } catch (Exception e) {
            showErrorMessage("Error returning to previous screen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validateInputs() {
        return !numberOfSeatsField.getText().isEmpty() &&
                !totalPriceField.getText().isEmpty() &&
                travelDatePicker.getValue() != null &&
                vehicleChoice.getValue() != null &&
                departStationChoice.getValue() != null &&
                finStationChoice.getValue() != null &&
                ticketCategoryChoice.getValue() != null &&
                statusChoice.getValue() != null &&
                travelDatePicker.getValue().isAfter(LocalDateTime.now().toLocalDate());
    }

    private void clearFields() {
        numberOfSeatsField.clear();
        totalPriceField.clear();
        travelDatePicker.setValue(null);
        vehicleChoice.getSelectionModel().clearSelection();
        departStationChoice.getSelectionModel().clearSelection();
        finStationChoice.getSelectionModel().clearSelection();
        ticketCategoryChoice.setValue(TicketCategory.ECONOMIC);
        statusChoice.setValue(ReservationStatus.CONFIRMED);
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

    // Placeholder methods - replace with actual service calls
    private List<Vehicule> getVehiclesFromService() {
        return Arrays.asList(new Vehicule()); // Replace with actual service call
    }

    private List<Station> getStationsFromService() {
        return Arrays.asList(new Station()); // Replace with actual service call
    }
}