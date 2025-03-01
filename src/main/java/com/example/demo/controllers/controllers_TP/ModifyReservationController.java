package com.example.demo.controllers.controllers_TP;

import com.example.demo.enums.enums_TP.ReservationStatus;
import com.example.demo.enums.enums_TP.TicketCategory;
import com.example.demo.models.models_TP.Reservation;
import com.example.demo.models.models_TP.Station;

import com.example.demo.models.models_TP.Vehicule;
import com.example.demo.services.services_TP.ReservationService;
import com.example.demo.services.services_TP.StationService;

import com.example.demo.services.services_TP.VehiculeService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class ModifyReservationController {

    @FXML private TextField numberOfSeatsField;
    @FXML private TextField totalPriceField;
    @FXML private DatePicker travelDatePicker;
    @FXML private ChoiceBox<TicketCategory> ticketCategoryChoice;
    @FXML private ChoiceBox<ReservationStatus> statusChoice;
    @FXML private ChoiceBox<Vehicule> vehicleChoice;
    @FXML private ChoiceBox<Station> departStationChoice;
    @FXML private ChoiceBox<Station> finStationChoice;
    @FXML private Button previousButton;
    @FXML private Button modifyButton;
    @FXML private Label messageLabel;

    private ReservationService reservationService = new ReservationService();
    private StationService stationService = new StationService();
    private VehiculeService vehiculesService = new VehiculeService();
    private Reservation reservationToModify;

    @FXML
    public void initialize() {
        // Populate choice boxes
        ticketCategoryChoice.getItems().addAll(TicketCategory.values());
        statusChoice.getItems().addAll(ReservationStatus.values());
        departStationChoice.setItems(FXCollections.observableArrayList(stationService.getAll()));
        finStationChoice.setItems(FXCollections.observableArrayList(stationService.getAll()));

        // Set converters for ChoiceBoxes
        departStationChoice.setConverter(new StringConverter<Station>() {
            @Override
            public String toString(Station station) {
                return station != null ? station.getNom() : "";
            }

            @Override
            public Station fromString(String string) {
                return null; // Not needed for display-only
            }
        });

        finStationChoice.setConverter(new StringConverter<Station>() {
            @Override
            public String toString(Station station) {
                return station != null ? station.getNom() : "";
            }

            @Override
            public Station fromString(String string) {
                return null; // Not needed for display-only
            }
        });

        vehicleChoice.setConverter(new StringConverter<Vehicule>() {
            @Override
            public String toString(Vehicule vehicule) {
                return vehicule != null ? vehicule.getType().toString() : ""; // Assuming getTypeName() exists
            }

            @Override
            public Vehicule fromString(String string) {
                return null; // Not needed for display-only
            }
        });

        // Add real-time input validation
        addTextValidation(numberOfSeatsField, "Number of seats is required.");
        addTextValidation(totalPriceField, "Total price is required.");

        // Validate ChoiceBox selections
        ticketCategoryChoice.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) {
                ticketCategoryChoice.setStyle("-fx-border-color: red;");
            } else {
                ticketCategoryChoice.setStyle("-fx-border-color: lightgray;");
            }
        });
        statusChoice.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) {
                statusChoice.setStyle("-fx-border-color: red;");
            } else {
                statusChoice.setStyle("-fx-border-color: lightgray;");
            }
        });
        vehicleChoice.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) {
                vehicleChoice.setStyle("-fx-border-color: red;");
            } else {
                vehicleChoice.setStyle("-fx-border-color: lightgray;");
            }
        });
        departStationChoice.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) {
                departStationChoice.setStyle("-fx-border-color: red;");
            } else {
                departStationChoice.setStyle("-fx-border-color: lightgray;");
                // Update vehicle choice based on departure station's line
                updateVehicleChoice(newValue.getLigne().getId());
            }
        });
        finStationChoice.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) {
                finStationChoice.setStyle("-fx-border-color: red;");
            } else {
                finStationChoice.setStyle("-fx-border-color: lightgray;");
            }
        });

        // Handle travel date selection
        travelDatePicker.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) {
                travelDatePicker.setStyle("-fx-border-color: red;");
            } else {
                travelDatePicker.setStyle("-fx-border-color: lightgray;");
            }
        });
    }

    public void setReservation(Reservation reservation) {
        this.reservationToModify = reservation;
        numberOfSeatsField.setText(String.valueOf(reservation.getNumberOfSeats()));
        totalPriceField.setText(String.valueOf(reservation.getTotalPrice()));
        travelDatePicker.setValue(reservation.getTravelDate().toLocalDate());
        ticketCategoryChoice.getSelectionModel().select(reservation.getTicketCategory());
        statusChoice.getSelectionModel().select(reservation.getStatus());
        vehicleChoice.getSelectionModel().select(reservation.getVehicule());
        departStationChoice.getSelectionModel().select(reservation.getDepartStation());
        finStationChoice.getSelectionModel().select(reservation.getFinStation());
    }

    @FXML
    private void handleModifyReservation() {
        if (!validateFields()) {
            return;
        }

        try {
            reservationToModify.setNumberOfSeats(Integer.parseInt(numberOfSeatsField.getText().trim()));
            reservationToModify.setTotalPrice(Double.parseDouble(totalPriceField.getText().trim()));
            reservationToModify.setTravelDate(travelDatePicker.getValue().atStartOfDay());
            reservationToModify.setTicketCategory(ticketCategoryChoice.getValue());
            reservationToModify.setStatus(statusChoice.getValue());
            reservationToModify.setVehicule(vehicleChoice.getValue());
            reservationToModify.setDepartStation(departStationChoice.getValue());
            reservationToModify.setFinStation(finStationChoice.getValue());

            reservationService.update(reservationToModify);
            showSuccessMessage("Reservation modified successfully!");
            goBack();
        } catch (NumberFormatException e) {
            showErrorMessage("Invalid number format for seats or price.");
        } catch (Exception e) {
            showErrorMessage("Error modifying reservation: " + e.getMessage());
        }
    }

    @FXML
    private void goBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Ressource-TP/ReservationManagement.fxml"));
            Stage stage = (Stage) previousButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Reservation Management");
        } catch (IOException e) {
            showErrorMessage("Error returning to previous screen: " + e.getMessage());
        }
    }

    private void addTextValidation(TextField field, String errorMessage) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isEmpty()) {
                field.setStyle("-fx-border-color: red;");
            } else {
                field.setStyle("-fx-border-color: lightgray;");
            }
        });
    }

    private boolean validateFields() {
        boolean isValid = true;

        // Check number of seats
        if (numberOfSeatsField.getText().trim().isEmpty()) {
            showErrorMessage("Number of seats is required.");
            numberOfSeatsField.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            try {
                int seats = Integer.parseInt(numberOfSeatsField.getText().trim());
                if (seats <= 0) {
                    showErrorMessage("Number of seats must be greater than 0.");
                    numberOfSeatsField.setStyle("-fx-border-color: red;");
                    isValid = false;
                } else {
                    numberOfSeatsField.setStyle("-fx-border-color: lightgray;");
                }
            } catch (NumberFormatException e) {
                showErrorMessage("Invalid number format for seats.");
                numberOfSeatsField.setStyle("-fx-border-color: red;");
                isValid = false;
            }
        }

        // Check total price
        if (totalPriceField.getText().trim().isEmpty()) {
            showErrorMessage("Total price is required.");
            totalPriceField.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            try {
                double price = Double.parseDouble(totalPriceField.getText().trim());
                if (price <= 0) {
                    showErrorMessage("Total price must be greater than 0.");
                    totalPriceField.setStyle("-fx-border-color: red;");
                    isValid = false;
                } else {
                    totalPriceField.setStyle("-fx-border-color: lightgray;");
                }
            } catch (NumberFormatException e) {
                showErrorMessage("Invalid number format for price.");
                totalPriceField.setStyle("-fx-border-color: red;");
                isValid = false;
            }
        }

        // Check travel date
        if (travelDatePicker.getValue() == null) {
            showErrorMessage("Travel date is required.");
            travelDatePicker.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            travelDatePicker.setStyle("-fx-border-color: lightgray;");
        }

        // Check ChoiceBoxes
        if (ticketCategoryChoice.getValue() == null) {
            showErrorMessage("Ticket category is required.");
            ticketCategoryChoice.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            ticketCategoryChoice.setStyle("-fx-border-color: lightgray;");
        }

        if (statusChoice.getValue() == null) {
            showErrorMessage("Status is required.");
            statusChoice.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            statusChoice.setStyle("-fx-border-color: lightgray;");
        }

        if (vehicleChoice.getValue() == null) {
            showErrorMessage("Vehicle is required.");
            vehicleChoice.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            vehicleChoice.setStyle("-fx-border-color: lightgray;");
        }

        if (departStationChoice.getValue() == null) {
            showErrorMessage("Departure station is required.");
            departStationChoice.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            departStationChoice.setStyle("-fx-border-color: lightgray;");
        }

        if (finStationChoice.getValue() == null) {
            showErrorMessage("Arrival station is required.");
            finStationChoice.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            finStationChoice.setStyle("-fx-border-color: lightgray;");
        }

        return isValid;
    }

    private void updateVehicleChoice(int lineId) {
        List<Vehicule> vehicles = vehiculesService.getVehiculesByLineId(lineId);
        vehicleChoice.setItems(FXCollections.observableArrayList(vehicles));
    }

    private void showSuccessMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setTextFill(Color.GREEN);
        messageLabel.setVisible(true);
        hideMessageAfterDelay();
    }

    private void showErrorMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setTextFill(Color.RED);
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