package com.example.demo.models.models_cov;

public class Covoiturage {
    private int id;
    private int id_user;
    private String point_de_depart;
    private String point_de_destination;
    private float prix;
    private int nbr_place;

    public Covoiturage(int id, int id_user, String point_de_depart, String point_de_destination, float prix, int nbr_place) {
        this.id = id;
        this.id_user = id_user;
        this.point_de_depart = point_de_depart;
        this.point_de_destination = point_de_destination;
        this.prix = prix;
        this.nbr_place = nbr_place;
    }

    public Covoiturage(int id_user, String point_de_depart, String point_de_destination, float prix, int nbr_place) {
        this.id_user = id_user;
        this.point_de_depart = point_de_depart;
        this.point_de_destination = point_de_destination;
        this.prix = prix;
        this.nbr_place = nbr_place;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getPoint_de_depart() {
        return point_de_depart;
    }

    public void setPoint_de_depart(String point_de_depart) {
        this.point_de_depart = point_de_depart;
    }

    public String getPoint_de_destination() {
        return point_de_destination;
    }

    public void setPoint_de_destination(String point_de_destination) {
        this.point_de_destination = point_de_destination;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public int getNbr_place() {
        return nbr_place;
    }

    public void setNbr_place(int nbr_place) {
        this.nbr_place = nbr_place;
    }
}
