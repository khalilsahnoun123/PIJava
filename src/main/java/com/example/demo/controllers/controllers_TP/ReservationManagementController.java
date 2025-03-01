package com.example.demo.controllers.controllers_TP;

import com.example.demo.enums.enums_TP.ReservationStatus;
import com.example.demo.enums.enums_TP.TicketCategory;
import com.example.demo.models.models_TP.Reservation;
import com.example.demo.services.services_TP.ReservationService;
import javafx.application.Platform;
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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReservationManagementController {

    @FXML private GridPane gridPane;
    @FXML private Label messageLabel;
    @FXML private Button addButton;
    @FXML private Button previousButton;


    private List<Reservation> reservations;
    private ReservationService reservationService = new ReservationService();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");



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
        for (int i = 0; i < 11; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(i == 0 ? 5 : i == 3 ? 7 : 10);
            gridPane.getColumnConstraints().add(col);
        }

        addHeaderRow();
    }
    private void addHeaderRow() {
        String[] headers = {"ID", "Reservation Date", "Travel Date", "Seats", "Category",
                "Status", "Total Price", "Vehicle", "Departure", "Arrival", "Actions"};
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

    public void loadData() {
        reservations = reservationService.getAll();
        refreshGrid();
    }

    private void refreshGrid() {
        Platform.runLater(() -> {
            gridPane.getChildren().removeIf(node ->
                    GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0
            );

            int rowIndex = 1;
            for (Reservation reservation : reservations) {
                addReservationRow(reservation, rowIndex);
                rowIndex++;
            }
        });
    }

    private void addReservationRow(Reservation reservation, int rowIndex) {
        // ID
        addGridCell(String.valueOf(reservation.getReservationId()), 0, rowIndex);

        // Dates
        addGridCell(reservation.getReservationDate().format(DATE_FORMATTER), 1, rowIndex);
        addGridCell(reservation.getTravelDate().format(DATE_FORMATTER), 2, rowIndex);

        // Numeric values
        addGridCell(String.valueOf(reservation.getNumberOfSeats()), 3, rowIndex);
        addGridCell(reservation.getTicketCategory().toString(), 4, rowIndex);
        addGridCell(reservation.getStatus().toString(), 5, rowIndex);
        addGridCell(String.format("$%.2f", reservation.getTotalPrice()), 6, rowIndex);

        // Related entities
        addGridCell(reservation.getVehicule() != null ?
                String.valueOf(reservation.getVehicule().getId()) : "N/A", 7, rowIndex);

        // Departure Station with Line
        String departStationName = reservation.getDepartStation() != null ? reservation.getDepartStation().getNom() : "";
        String departLineName = reservation.getDepartStation() != null && reservation.getDepartStation().getLigne() != null ?
                reservation.getDepartStation().getLigne().getName() : "";
        String departStationInfo = departStationName.isEmpty() || departLineName.isEmpty() ? "N/A" :
                departStationName + " (" + departLineName + ")";
        addGridCell(departStationInfo, 8, rowIndex);

        // Arrival Station with Line
        String finStationName = reservation.getFinStation() != null ? reservation.getFinStation().getNom() : "";
        String finLineName = reservation.getFinStation() != null && reservation.getFinStation().getLigne() != null ?
                reservation.getFinStation().getLigne().getName() : "";
        String finStationInfo = finStationName.isEmpty() || finLineName.isEmpty() ? "N/A" :
                finStationName + " (" + finLineName + ")";
        addGridCell(finStationInfo, 9, rowIndex);

        // Action Buttons
        HBox actionBox = new HBox(10);
        Button editButton = new Button("Edit");
        Button deleteButton = new Button("Delete");
        Button statusButton;

        // Conditionally add "Cancel" or "Uncancel" button based on status
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            statusButton = new Button("Uncancel");
            statusButton.setOnAction(e -> handleUncancel(reservation));
        } else {
            statusButton = new Button("Cancel");
            statusButton.setOnAction(e -> handleCancel(reservation));
        }

        editButton.getStyleClass().add("grid-action-button");
        deleteButton.getStyleClass().add("grid-action-button");
        statusButton.getStyleClass().add("grid-action-button");

        editButton.setOnAction(e -> handleEdit(reservation));
        deleteButton.setOnAction(e -> handleDelete(reservation));

        actionBox.getChildren().addAll(editButton, deleteButton, statusButton);
        gridPane.add(actionBox, 10, rowIndex);

        // Hover effect
        applyRowHoverEffect(rowIndex);
    }

    private void handleCancel(Reservation reservation) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Cancel Reservation #" + reservation.getReservationId());
        alert.setContentText("Are you sure you want to cancel this reservation?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            reservation.setStatus(ReservationStatus.CANCELLED);
            reservationService.update(reservation);
            showSuccessMessage("Reservation canceled successfully!");
            loadData();
        }
    }

    private void handleUncancel(Reservation reservation) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Uncancel Reservation #" + reservation.getReservationId());
        alert.setContentText("Are you sure you want to uncancel this reservation?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            // Check vehicle ID and set status accordingly
            if (reservation.getVehicule() != null) {
                reservation.setStatus(ReservationStatus.CONFIRMED);
            } else {
                reservation.setStatus(ReservationStatus.PENDING);
            }
            reservationService.update(reservation);
            showSuccessMessage("Reservation uncanceled successfully!");
            loadData();
        }
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

    private void handleEdit(Reservation reservation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ressource-TP/ModifyReservation.fxml"));
            Parent root = loader.load();
            ModifyReservationController controller = loader.getController();
            controller.setReservation(reservation);

            Stage stage = (Stage) gridPane.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showErrorMessage("Error: " + e.getMessage());
        }
    }

    private void handleDelete(Reservation reservation) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Delete Reservation #" + reservation.getReservationId());
        alert.setContentText("Are you sure you want to delete this reservation?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            reservationService.delete(reservation.getReservationId());
            showSuccessMessage("Reservation deleted successfully!");
            loadData();
        }
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