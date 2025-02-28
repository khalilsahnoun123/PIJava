package com.example.demo.models.models_TP;

import com.example.demo.enums.enums_TP.Region;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ligne")
public class Ligne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    private String name;

    private Double PrixVIP;
    private Double PrixPREMIUM;
    private Double PrixECONIMIC;


    private Region region;

    @OneToMany(mappedBy = "ligne", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Station> stations;

    @OneToMany(mappedBy = "ligne", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vehicule> vehicules;



    // Getters and setters
}

