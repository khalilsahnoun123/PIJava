package com.example.demo.models.models_velo;

public class StationVelo {
    private int idStation;
    private String nameStation;
    private String gouvernera;
    private String municapilite;
    private String adresse;
    private User admin;  // Object reference instead of int id_admin
    private String stationImage;

    // Constructors
    public StationVelo() {}

    public StationVelo(int idStation, String nameStation, String gouvernera,
                       String municapilite, String adresse, User admin,
                       String stationImage) {
        this.idStation = idStation;
        this.nameStation = nameStation;
        this.gouvernera = gouvernera;
        this.municapilite = municapilite;
        this.adresse = adresse;
        this.admin = admin;
        this.stationImage = stationImage;
    }

    // Getters and Setters
    public int getIdStation() { return idStation; }
    public void setIdStation(int idStation) { this.idStation = idStation; }

    public String getNameStation() { return nameStation; }
    public void setNameStation(String nameStation) { this.nameStation = nameStation; }

    public String getGouvernera() { return gouvernera; }
    public void setGouvernera(String gouvernera) { this.gouvernera = gouvernera; }

    public String getMunicapilite() { return municapilite; }
    public void setMunicapilite(String municapilite) { this.municapilite = municapilite; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public User getAdmin() { return admin; }
    public void setAdmin(User admin) { this.admin = admin; }

    public String getStationImage() { return stationImage; }
    public void setStationImage(String stationImage) { this.stationImage = stationImage; }

    @Override
    public String toString() {
        return "StationVelo{" +
                "idStation=" + idStation +
                ", nameStation='" + nameStation + '\'' +
                ", gouvernera='" + gouvernera + '\'' +
                ", municapilite='" + municapilite + '\'' +
                ", adresse='" + adresse + '\'' +
                ", admin=" + admin.getNom() + // Display admin's username
                ", stationImage='" + stationImage + '\'' +
                '}';
    }
}