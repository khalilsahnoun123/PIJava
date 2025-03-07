package com.example.demo.controllers.controllers_velo;

import com.example.demo.tests.main;
import com.example.demo.models.models_velo.*;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import com.example.demo.services.services_velo.PaymentService;
import com.example.demo.services.services_velo.ReservationVeloService;
import com.example.demo.services.services_velo.VeloService;
import com.example.demo.services.services_velo.UserService;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class BikeSelectionController implements Initializable {

    @FXML private DatePicker endDatePicker;
    @FXML private ComboBox<Integer> hoursCombo;
    @FXML private ComboBox<Integer> minutesCombo;
    @FXML private FlowPane bikesContainer;
    @FXML private Label priceLabel;
    User user = new User();
    UserService userService = new UserService();
    private Velo selectedBike;
    private StationVelo currentStation;
    private final VeloService veloService = new VeloService();
    private final ReservationVeloService reservationService = new ReservationVeloService();
    private final PaymentService paymentService = new PaymentService("67c0ceaedeff4671bb44b90c:7iGgxqBEY8oqUGXjP");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTimeCombos();
        setupListeners();
    }

    private void setupTimeCombos() {
        // Remplir les combobox d'heures et minutes
        hoursCombo.getItems().addAll(IntStream.rangeClosed(0, 23).boxed().toList());
        minutesCombo.getItems().addAll(0, 15, 30, 45);

        // Définir les valeurs par défaut
        hoursCombo.setValue(LocalDateTime.now().getHour());
        minutesCombo.setValue(0);
    }

    private void setupListeners() {
        endDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> updatePrice());
        hoursCombo.valueProperty().addListener((obs, oldVal, newVal) -> updatePrice());
        minutesCombo.valueProperty().addListener((obs, oldVal, newVal) -> updatePrice());
    }

    public void initializeWithStation(StationVelo station) {
        this.currentStation = station;
        loadAvailableBikes();
    }

    private void loadAvailableBikes() {
        bikesContainer.getChildren().clear();
        veloService.getAvailableBikesByStation(currentStation.getIdStation())
                .forEach(this::createBikeCard);
    }

    private LocalDateTime getMaxEndDate() {
        return LocalDateTime.now().plusHours(72);
    }

    // BikeSelectionController.java
    private void createBikeCard(Velo bike) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resource-Velo/BikeCard.fxml"));
            Parent card = loader.load();

            BikeCardController controller = loader.getController();
            controller.setBikeData(bike);
            controller.setParentController(this); // Lien critique

            card.setOnMouseClicked(e -> {
                System.out.println("Carte cliquée - Vélo ID: " + bike.getIdVelo());
                selectBike(bike);
            });

            bikesContainer.getChildren().add(card);
        } catch (IOException ex) {
            System.err.println("Erreur de chargement de la carte:");
            ex.printStackTrace();
        }
    }

    private void selectBike(Velo bike) {
        this.selectedBike = bike;
        updatePrice();
    }

    private void updatePrice() {
        if (!isSelectionValid()) return;

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = getSelectedEndDateTime();

        if (isDurationExceeded(start, end)) {
            showAlert("Durée invalide", "La durée maximale est de 72 heures");
            return;
        }

        double price = calculatePrice(start, end);
        priceLabel.setText(String.format("Prix total : %.2f DT", price));
    }

    private LocalDateTime getSelectedEndDateTime() {
        return LocalDateTime.of(
                endDatePicker.getValue(),
                LocalTime.of(hoursCombo.getValue(), minutesCombo.getValue())
        );
    }

    private boolean isSelectionValid() {
        return selectedBike != null &&
                endDatePicker.getValue() != null &&
                hoursCombo.getValue() != null &&
                minutesCombo.getValue() != null;
    }

    private boolean isDurationExceeded(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toHours() > 72;
    }

    private double calculatePrice(LocalDateTime start, LocalDateTime end) {
        long minutes = Duration.between(start, end).toMinutes();
        double hours = Math.ceil(minutes / 15.0) * 0.25; // Arrondi aux 15 minutes supérieures
        return hours * selectedBike.getType().getPrice();
    }

    @FXML
    private void handleReservation() {
        if (!validateReservation()) return;

        ReservationVelo reservation = createReservation();

        if (reservationService.createReservation(reservation)) {
            showConfirmation("Réservation confirmée !");
            loadAvailableBikes();
        } else {
            showAlert("Erreur", "Ce vélo n'est plus disponible");
        }
    }

    private boolean validateReservation() {
        if (selectedBike == null) {
            showAlert("Sélection requise", "Veuillez sélectionner un vélo");
            return false;
        }

        LocalDateTime end = getSelectedEndDateTime();

        if (end.isBefore(LocalDateTime.now().plusMinutes(15))) {
            showAlert("Horaire invalide", "La réservation doit durer au moins 15 minutes");
            return false;
        }

        return true;
    }

    private ReservationVelo createReservation() {
        user = userService.readById(1);
        ReservationVelo reservation = new ReservationVelo();
        reservation.setVelo(selectedBike);
        reservation.setUser(user);
        reservation.setDateDebut(LocalDateTime.now());
        reservation.setDateFin(getSelectedEndDateTime());
        reservation.setPrice(calculatePrice(LocalDateTime.now(), getSelectedEndDateTime()));
        return reservation;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML private Label totalPriceLabel;
    @FXML private Button confirmButton;

    // BikeSelectionController.java
    public void initReservation(Velo bike) {
        System.out.println("Initialisation réservation pour vélo type: " + bike.getType().getTypeName());

        this.selectedBike = bike;
        updatePriceDisplay();
        confirmButton.setDisable(false);

        // Debug supplémentaire
        System.out.println("Vélo sélectionné: " + (selectedBike != null ? selectedBike.getIdVelo() : "null"));
        System.out.println("Bouton confirm activé: " + !confirmButton.isDisabled());
    }

    private void updatePriceDisplay() {
        if (selectedBike == null) return;

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = getSelectedEndDateTime();

        double price = calculatePrice(start, end);
        totalPriceLabel.setText(String.format("Total: %.2f DT", price));
        totalPriceLabel.setVisible(true);
    }

    @FXML
    private void handleConfirmReservation() {
        System.out.println("Tentative de confirmation...");

        if (!validateReservation()) {return;}

        ReservationVelo reservation = createReservation();
        System.out.println("Création réservation temporaire: " + reservation);

        // 1. Create payment request
        InitiatePaymentRequest paymentRequest = new InitiatePaymentRequest(
                "67c0ceaedeff4671bb44b914", // Replace with your wallet ID
                reservation.getPrice() * 1000 // Convert DT to millimes
        );

        try {
            // 2. Initiate payment
            InitiatePaymentResponse paymentResponse = paymentService.initiatePayment(paymentRequest);

            // 3. Open payment gateway in WebView
            openPaymentPage(paymentResponse.getPayUrl());

            // 4. Start payment monitoring
            startPaymentMonitoring(paymentResponse.getPaymentRef(), reservation);

        } catch (Exception e) {
            System.out.println("Échec de l'initialisation du paiement: " + e.getMessage());
        }
    }

    private void startPaymentMonitoring(String paymentRef, ReservationVelo reservation) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        // Show loading indicator
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(true);
        bikesContainer.getChildren().add(progressIndicator);

        scheduler.scheduleAtFixedRate(() -> {
            try {
                PaymentDetailsResponse details = paymentService.getPaymentStatus(paymentRef);
// Accès sécurisé avec vérifications de nullité
                if(details.getPayment() != null
                        && details.getPayment().getTransactions() != null
                        && !details.getPayment().getTransactions().isEmpty()) {

                    PaymentDetailsResponse.Payment.Transaction firstTransaction =
                            details.getPayment().getTransactions().get(0);

                    if(firstTransaction.getExtSenderInfo() != null) {
                        String senderName = firstTransaction.getExtSenderInfo().getName();
                        System.out.println("Sender name: " + senderName);
                    }
                }                if ("completed".equalsIgnoreCase(details.getPayment().getStatus())) {
                    Platform.runLater(() -> {
                        // 5. Finalize reservation after successful payment
                        if (reservationService.createReservation(reservation)) {
                            System.out.println("Réservation et paiement réussis !");
                            showConfirmation("Succès",
                                    "Paiement confirmé et réservation effectuée pour le vélo ID: " +
                                            selectedBike.getIdVelo());
                            loadAvailableBikes();
                            resetForm();
                        } else {
                            showErrorAlert("Erreur",
                                    "Paiement réussi mais échec de la réservation. Contactez le support.");
                        }
                        progressIndicator.setVisible(false);
                        scheduler.shutdown();
                    });
                }

            } catch (Exception e) {
                Platform.runLater(() -> {
                    showErrorAlert("Erreur de vérification", e.getMessage());
                    System.out.println("Erreur de vérification:"+ e.getMessage());

                    progressIndicator.setVisible(false);
                    scheduler.shutdown();
                });
            }
        }, 0, 10, TimeUnit.SECONDS);

        // Add timeout handling
        scheduler.schedule(() -> {
            if (!scheduler.isShutdown()) {
                Platform.runLater(() -> {
                    showErrorAlert("Timeout", "Paiement non complété à temps");
                    progressIndicator.setVisible(false);
                    scheduler.shutdown();
                });
            }
        }, 1, TimeUnit.HOURS);
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void resetForm() {
        confirmButton.setDisable(true);
        totalPriceLabel.setVisible(false);
        endDatePicker.setValue(null);
        hoursCombo.getSelectionModel().clearSelection();
        minutesCombo.getSelectionModel().clearSelection();
    }

    private void showConfirmation(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private void openPaymentPage(String url) {
        Stage paymentStage = new Stage();
        paymentStage.setTitle("Paiement");

        // Create WebView for payment
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.load(url); // Load the payment URL

        // Add a close button
        Button closeButton = new Button("Fermer");
        closeButton.setOnAction(e -> paymentStage.close());

        // Create a layout to hold the WebView and close button
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(webView, closeButton);

        // Create a scene and show the stage
        Scene scene = new Scene(layout, 900, 600);
        paymentStage.setScene(scene);
        paymentStage.show();

        System.out.println("Payment page opened in WebView.");
    }
}