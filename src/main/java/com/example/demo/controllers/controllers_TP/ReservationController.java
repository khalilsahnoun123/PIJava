package com.example.demo.controllers.controllers_TP;

import com.example.demo.enums.enums_TP.ReservationStatus;
import com.example.demo.enums.enums_TP.TicketCategory;
import com.example.demo.models.models_TP.Reservation;
import com.example.demo.models.models_TP.Vehicule;
import com.example.demo.services.services_TP.ReservationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class ReservationController {

    @FXML
    private DatePicker dpReservationDate;
    @FXML
    private DatePicker dpTravelDate;
    @FXML
    private TextField tfNumberOfSeats;
    @FXML
    private ComboBox<TicketCategory> cbTicketCategory;
    @FXML
    private ComboBox<ReservationStatus> cbStatus;
    @FXML
    private TextField tfTotalPrice;
    @FXML
    private TextField tfVehiculeId;
    @FXML
    private Label lbReservations;

    private ReservationService reservationService = new ReservationService();

    @FXML
    public void initialize() {
        cbTicketCategory.getItems().setAll(TicketCategory.values());
        cbStatus.getItems().setAll(ReservationStatus.values());
        afficherReservations();
    }

    @FXML
    public void ajouterReservation(ActionEvent actionEvent) {
        try {
            Reservation reservation = new Reservation();
            reservation.setReservationDate(convertToDate(dpReservationDate.getValue()));
            reservation.setTravelDate(convertToDate(dpTravelDate.getValue()));
            reservation.setNumberOfSeats(Integer.parseInt(tfNumberOfSeats.getText()));
            reservation.setTicketCategory(cbTicketCategory.getValue());
            reservation.setStatus(cbStatus.getValue());
            reservation.setTotalPrice(Double.parseDouble(tfTotalPrice.getText()));

            int vehiculeId = Integer.parseInt(tfVehiculeId.getText());
            Vehicule vehicule = new Vehicule();
            vehicule.setId(vehiculeId);
            reservation.setVehicule(vehicule);

            reservationService.add(reservation);
            afficherReservations();
        } catch (NumberFormatException e) {
            System.out.println("Erreur de format : " + e.getMessage());
        }
    }

    @FXML
    public void afficherReservations() {
        lbReservations.setText(reservationService.getAll().toString());
    }

    private LocalDateTime convertToDate(LocalDate localDate) {
        return LocalDateTime.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
