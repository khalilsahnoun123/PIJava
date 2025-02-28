package com.example.demo.controllers.controllers_TP;

import com.example.demo.enums.enums_TP.ReservationStatus;
import com.example.demo.enums.enums_TP.TicketCategory;
import com.example.demo.models.models_TP.Ligne; // Assume this entity exists
import com.example.demo.models.models_TP.Reservation;
import com.example.demo.models.models_TP.Station;
import com.example.demo.models.models_TP.Vehicule;
import com.example.demo.services.services_TP.LigneService;
import com.example.demo.services.services_TP.ReservationService;
import com.example.demo.services.services_TP.StationService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class AddReservationController {

    @FXML private TextField numberOfSeatsField;
    @FXML private ChoiceBox<TicketCategory> ticketCategoryChoice;
    @FXML private ChoiceBox<Ligne> lineChoice;
    @FXML private ChoiceBox<Station> departStationChoice;
    @FXML private ChoiceBox<Station> finStationChoice;
    @FXML private DatePicker travelDatePicker;
    @FXML private Button addButton;
    @FXML private Button previousButton;
    @FXML private Label messageLabel;

    private LigneService ligneService = new LigneService();
    private StationService stationService = new StationService();
    private ReservationService reservationService = new ReservationService();
    private Vehicule selectedVehicule; // To store the automatically selected vehicle

    @FXML
    public void initialize() {
        // Initialize ticket category ChoiceBox
        ticketCategoryChoice.getItems().addAll(TicketCategory.values());
        ticketCategoryChoice.setValue(TicketCategory.ECONOMIC);

        // Initialize line ChoiceBox and set converter to show only the line name
        List<Ligne> lines = getLinesFromService();
        lineChoice.setItems(FXCollections.observableArrayList(lines));
        lineChoice.setConverter(new StringConverter<Ligne>() {
            @Override
            public String toString(Ligne ligne) {
                return ligne == null ? "" : ligne.getName();
            }
            @Override
            public Ligne fromString(String string) {
                return null; // Not needed
            }
        });

        // Set converters for stations to display only the station name
        departStationChoice.setConverter(new StringConverter<Station>() {
            @Override
            public String toString(Station station) {
                return station == null ? "" : station.getNom();
            }
            @Override
            public Station fromString(String string) {
                return null;
            }
        });
        finStationChoice.setConverter(new StringConverter<Station>() {
            @Override
            public String toString(Station station) {
                return station == null ? "" : station.getNom();
            }
            @Override
            public Station fromString(String string) {
                return null;
            }
        });

        // When a line is selected, populate station ChoiceBoxes for both departure and arrival
        lineChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                List<Station> stations = getStationsForLine(newValue);
                departStationChoice.getItems().setAll(stations);
                finStationChoice.getItems().setAll(stations);
                // Optionally set selectedVehicule here based on the selected line
                // selectedVehicule = getVehicleForLine(newValue);
            }
        });

        // Input validation for number of seats (only digits allowed)
        numberOfSeatsField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                numberOfSeatsField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        // Style buttons (example)
        styleComponents();
    }

    private void styleComponents() {
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        previousButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");

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
            int numberOfSeats = Integer.parseInt(numberOfSeatsField.getText());
            double totalPrice = numberOfSeats * getPricePerSeat(ticketCategoryChoice.getValue());

            Reservation reservation = Reservation.builder()
                    .reservationDate(LocalDateTime.now())
                    .travelDate(travelDatePicker.getValue().atStartOfDay())
                    .numberOfSeats(numberOfSeats)
                    .ticketCategory(ticketCategoryChoice.getValue())
                    .status(ReservationStatus.CONFIRMED)
                    .totalPrice(totalPrice)
                    .vehicule(selectedVehicule) // This may be null if not auto-assigned; remove if unnecessary
                    .departStation(departStationChoice.getValue())
                    .finStation(finStationChoice.getValue())
                    .build();

            reservationService.add(reservation);
            showSuccessMessage("Reservation added successfully!");
            clearFields();
            goBack();
        } catch (Exception e) {
            showErrorMessage("Error adding reservation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePrevious() {
        goBack();
    }

    private void goBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Ressource-TP/ReservationManagement.fxml"));
            Stage stage = (Stage) previousButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Reservation Management");
        } catch (IOException e) {
            showErrorMessage("Error returning to previous screen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validateInputs() {
        // Check number of seats
        if (numberOfSeatsField.getText().isEmpty() || Integer.parseInt(numberOfSeatsField.getText()) <= 0) {
            return false;
        }
        // Check travel date: must not be null and must be after today
        if (travelDatePicker.getValue() == null || !travelDatePicker.getValue().isAfter(LocalDateTime.now().toLocalDate())) {
            return false;
        }
        // Check that ticket category, line, and stations are selected
        if (ticketCategoryChoice.getValue() == null || lineChoice.getValue() == null ||
                departStationChoice.getValue() == null || finStationChoice.getValue() == null) {
            return false;
        }
        // Check if departure and arrival stations are the same by comparing their IDs
        if (departStationChoice.getValue().getId() == finStationChoice.getValue().getId()) {
            showErrorMessage("Departure and Arrival stations cannot be the same. Please select different stations.");
            return false;
        }
        // Remove or modify this check if selectedVehicule is auto-assigned later
        // if (selectedVehicule == null) {
        //     return false;
        // }
        return true;
    }

    private void clearFields() {
        numberOfSeatsField.clear();
        travelDatePicker.setValue(null);
        ticketCategoryChoice.setValue(TicketCategory.ECONOMIC);
        lineChoice.getSelectionModel().clearSelection();
        departStationChoice.getItems().clear();
        finStationChoice.getItems().clear();
        // Optionally reset selectedVehicule if needed
        // selectedVehicule = null;
    }

    private double getPricePerSeat(TicketCategory category) {
        switch (category) {
            case ECONOMIC:
                return 10.0;
            case PREMIUM:
                return 20.0;
            case VIP:
                return 30.0;
            default:
                return 0.0;
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

    // Placeholder service methods - replace with actual implementations
    private List<Ligne> getLinesFromService() {
        return ligneService.getAll(); // Replace with actual service call
    }

    private List<Station> getStationsForLine(Ligne ligne) {
        return stationService.getStationByIdLigne(ligne.getId()); // Replace with actual filtering logic
    }
}
