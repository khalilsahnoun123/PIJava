package com.example.demo.models.models_taxi;

public class Vehicule {
    private int id;
    private int id_chauffeur;
    private String marque;
    private String modele;
    private String immatriculation;
    private String numero_de_serie;

    public Vehicule(int id, int id_chauffeur, String marque, String modele, String immatriculation, String numero_de_serie) {
        this.id = id;
        this.id_chauffeur = id_chauffeur;
        this.marque = marque;
        this.modele = modele;
        this.immatriculation = immatriculation;
        this.numero_de_serie = numero_de_serie;
    }

    public Vehicule(int id_chauffeur, String marque, String modele, String immatriculation, String numero_de_serie) {
        this.id_chauffeur = id_chauffeur;
        this.marque = marque;
        this.modele = modele;
        this.immatriculation = immatriculation;
        this.numero_de_serie = numero_de_serie;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_chauffeur() {
        return id_chauffeur;
    }

    public void setId_chauffeur(int id_chauffeur) {
        this.id_chauffeur = id_chauffeur;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public String getNumero_de_serie() {
        return numero_de_serie;
    }

    public void setNumero_de_serie(String numero_de_serie) {
        this.numero_de_serie = numero_de_serie;
    }
}
