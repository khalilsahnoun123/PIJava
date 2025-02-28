package com.example.demo.models.models_TP;

import com.example.demo.enums.enums_TP.TypeVehicule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vehicules")
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    private TypeVehicule type;

    @Column(name = "nbr_max_passagers_vip")
    private int nbrMaxPassagersVIP;
    private int nbrMaxPassagersPremium;
    private int nbrMaxPassagersEconomy;
    @ManyToOne
    @JoinColumn(name = "ligne_id")
    private Ligne ligne;

    @Column(name = "places_disponibles_vip")
    private int placesDisponiblesVIP;
    private int placesDisponiblesPremium;
    private int placesDisponiblesEconomy;




    // Constructeurs, getters et setters
}
