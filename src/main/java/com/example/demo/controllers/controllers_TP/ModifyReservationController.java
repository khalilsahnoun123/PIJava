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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class ModifyReservationController {

    @FXML private TextField numberOfSeatsField;
    @FXML private TextField totalPriceField;
    @FXML private ChoiceBox<TicketCategory> ticketCategoryChoice;
    @FXML private ChoiceBox<ReservationStatus> statusChoice;
    @FXML private ChoiceBox<Vehicule> vehicleChoice;
    @FXML private ChoiceBox<Station> departStationChoice;
    @FXML private ChoiceBox<Station> finStationChoice;
    @FXML private DatePicker travelDatePicker;
    @FXML private Button modifyButton;
    @FXML private Button previousButton;
    @FXML private Label messageLabel;

    private ReservationService reservationService = new ReservationService();
    private Reservation reservationToModify;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML
    public void initialize() {
        // Initialize choice boxes
        ticketCategoryChoice.getItems().addAll(TicketCategory.values());
        statusChoice.getItems().addAll(ReservationStatus.values());
        vehicleChoice.getItems().addAll(getVehiclesFromService());
        departStationChoice.getItems().addAll(getStationsFromService());
        finStationChoice.getItems().addAll(getStationsFromService());

        // Setup input validation
        setupInputValidation();

        // Style components
        styleComponents();
    }

    public void setReservationToModify(Reservation reservation) {
        this.reservationToModify = reservation;
        populateFields();
    }

    private void populateFields() {
        if (reservationToModify != null) {
            numberOfSeatsField.setText(String.valueOf(reservationToModify.getNumberOfSeats()));
            totalPriceField.setText(String.valueOf(reservationToModify.getTotalPrice()));
            ticketCategoryChoice.setValue(reservationToModify.getTicketCategory());
            statusChoice.setValue(reservationToModify.getStatus());
            vehicleChoice.setValue(reservationToModify.getVehicule());
            departStationChoice.setValue(reservationToModify.getDepartStation());
            finStationChoice.setValue(reservationToModify.getFinStation());
            travelDatePicker.setValue(reservationToModify.getTravelDate().toLocalDate());
        }
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
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);

        // Style buttons
        modifyButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        previousButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white;");

        // Add hover effects
        modifyButton.setOnMouseEntered(e -> modifyButton.setStyle("-fx-background-color: #1976D2; -fx-text-fill: white;"));
        modifyButton.setOnMouseExited(e -> modifyButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;"));
        previousButton.setOnMouseEntered(e -> previousButton.setStyle("-fx-background-color: #757575; -fx-text-fill: white;"));
        previousButton.setOnMouseExited(e -> previousButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white;"));
    }

    @FXML
    private void handleModifyReservation() {
        if (!validateInputs()) {
            showErrorMessage("Please fill all required fields correctly");
            return;
        }

        try {
            reservationToModify.setNumberOfSeats(Integer.parseInt(numberOfSeatsField.getText()));
            reservationToModify.setTotalPrice(Double.parseDouble(totalPriceField.getText()));
            reservationToModify.setTicketCategory(ticketCategoryChoice.getValue());
            reservationToModify.setStatus(statusChoice.getValue());
            reservationToModify.setVehicule(vehicleChoice.getValue());
            reservationToModify.setDepartStation(departStationChoice.getValue());
            reservationToModify.setFinStation(finStationChoice.getValue());
            reservationToModify.setTravelDate(travelDatePicker.getValue().atStartOfDay());

            reservationService.update(reservationToModify);
            showSuccessMessage("Reservation modified successfully!");
        } catch (Exception e) {
            showErrorMessage("Error modifying reservation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePrevious() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Ressource-TP/ReservationManagement.fxml"));
            Stage stage = (Stage) previousButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Reservation Management");
        } catch (IOException e) {
            showErrorMessage("Error returning to management screen: " + e.getMessage());
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
    @FXML
    private void goBack() {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("/Ressource-TP/ReservationManagement.fxml"));
            Stage stage = (Stage) previousButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestion des Véhicules");
        } catch (IOException e) {
            showErrorMessage("Erreur lors du retour: " + e.getMessage());
        }
    }
}