package com.example.demo.models.models_TP;

import com.example.demo.enums.enums_TP.ReservationStatus;
import com.example.demo.enums.enums_TP.TicketCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reservationId;

    @Column(nullable = false)
    private LocalDateTime  reservationDate;

    @Column(nullable = false)
    private LocalDateTime  travelDate;

    @Column(nullable = false)
    private int numberOfSeats;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketCategory ticketCategory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;


    private double totalPrice;


    @ManyToOne
    @JoinColumn(name = "vehicule_id", nullable = false)
    private Vehicule vehicule;
    @ManyToOne
    @JoinColumn(name = "depart_station_id", nullable = false)
    private Station departStation;

    @ManyToOne
    @JoinColumn(name = "fin_station_id", nullable = false)
    private Station finStation;
    // Getters and setters
}

