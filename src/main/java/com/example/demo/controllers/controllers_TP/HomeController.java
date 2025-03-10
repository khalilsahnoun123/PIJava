package com.example.demo.controllers.controllers_TP;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {

    @FXML private Button reservationManagementButton;
    @FXML private Button stationManagementButton;
    @FXML private Button vehicleManagementButton;
    @FXML private Button ligneManagementButton;
    @FXML private Label welcomeLabel;

    @FXML
    public void initialize() {
        // Style buttons
        styleButtons();


        // Set welcome message
        welcomeLabel.setText("Welcome to Transportation Management System");
    }

    private void styleButtons() {
        // Common button styling
        String baseStyle = "-fx-font-size: 14px; -fx-text-fill: white; -fx-pref-width: 200px; -fx-pref-height: 40px;";

        reservationManagementButton.setStyle(baseStyle + "-fx-background-color: #4CAF50;");
        stationManagementButton.setStyle(baseStyle + "-fx-background-color: #2196F3;");
        vehicleManagementButton.setStyle(baseStyle + "-fx-background-color: #F44336;");
        ligneManagementButton.setStyle(baseStyle + "-fx-background-color: #9C27B0;");

        // Hover effects
        reservationManagementButton.setOnMouseEntered(e -> reservationManagementButton.setStyle(baseStyle + "-fx-background-color: #45a049;"));
        reservationManagementButton.setOnMouseExited(e -> reservationManagementButton.setStyle(baseStyle + "-fx-background-color: #4CAF50;"));

        stationManagementButton.setOnMouseEntered(e -> stationManagementButton.setStyle(baseStyle + "-fx-background-color: #1976D2;"));
        stationManagementButton.setOnMouseExited(e -> stationManagementButton.setStyle(baseStyle + "-fx-background-color: #2196F3;"));

        vehicleManagementButton.setOnMouseEntered(e -> vehicleManagementButton.setStyle(baseStyle + "-fx-background-color: #D32F2F;"));
        vehicleManagementButton.setOnMouseExited(e -> vehicleManagementButton.setStyle(baseStyle + "-fx-background-color: #F44336;"));

        ligneManagementButton.setOnMouseEntered(e -> ligneManagementButton.setStyle(baseStyle + "-fx-background-color: #7B1FA2;"));
        ligneManagementButton.setOnMouseExited(e -> ligneManagementButton.setStyle(baseStyle + "-fx-background-color: #9C27B0;"));
    }

    @FXML
    private void handleReservationManagement() {
        navigateTo("/Ressource-TP/ReservationManagement.fxml", "Reservation Management");
    }

    @FXML
    private void handleStationManagement() {
        navigateTo("/Ressource-TP/StationManagement.fxml", "Station Management");
    }

    @FXML
    private void handleVehicleManagement() {
        navigateTo("/com/example/demo/VehicleManagement.fxml", "Vehicle Management");
    }

    @FXML
    private void handleLigneManagement() {
        navigateTo("/Ressource-TP/LigneManagement.fxml", "Ligne Management");
    }

    private void navigateTo(String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) reservationManagementButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
        } catch (IOException e) {
            showErrorMessage("Error navigating to " + title + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showErrorMessage(String message) {
        Label errorLabel = new Label(message);
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        VBox root = (VBox) reservationManagementButton.getParent();
        if (!root.getChildren().contains(errorLabel)) {
            root.getChildren().add(errorLabel);
            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    root.getChildren().remove(errorLabel);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}