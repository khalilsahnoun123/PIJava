package com.example.demo.models.models_velo;

import java.time.LocalDateTime;

public class ReservationVelo {
    private int idReservationVelo;
    private User user;
    private Velo velo;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private double price;

    // Update setter

    private String statut;
    private boolean paiementEffectue;

    // Constructors
    public ReservationVelo() {}

    public ReservationVelo(int idReservationVelo, User user, Velo velo,
                           LocalDateTime dateDebut, LocalDateTime dateFin,
                           String statut, boolean paiementEffectue,double price) {
        this.idReservationVelo = idReservationVelo;
        this.user = user;
        this.velo = velo;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
        this.paiementEffectue = paiementEffectue;
        this.price = price;
    }

    // Getters and Setters
    public int getIdReservationVelo() {
        return idReservationVelo;
    }

    public void setIdReservationVelo(int idReservationVelo) {
        this.idReservationVelo = idReservationVelo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Velo getVelo() {
        return velo;
    }

    public void setVelo(Velo velo) {
        this.velo = velo;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }
    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public boolean isPaiementEffectue() {
        return paiementEffectue;
    }

    public void setPaiementEffectue(boolean paiementEffectue) {
        this.paiementEffectue = paiementEffectue;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ReservationVelo{" +
                "idReservationVelo=" + idReservationVelo +
                ", user=" + user.getNom() +
                ", velo=" + velo.getIdVelo() +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", statut='" + statut + '\'' +
                ", paiementEffectue=" + paiementEffectue +
                '}';
    }
}