package com.example.demo.models.models_velo;

public class User {
    private  int id_user;
    private String nom;
    private String gouvernorat;
    private String municipalite;
    private String adresse;

    public User() {
    }

    public User(int id_user, String nom) {
        this.id_user = id_user;
        this.nom = nom;

        //this.urlimage = urlimage;
    }



    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getNom() { return nom; }

    public void setNom(String username) { this.nom = username; }

    public String getGouvernorat() { return gouvernorat; }
    public void setGouvernorat(String gouvernorat) {
        this.gouvernorat = gouvernorat;
    }
    public String getMunicipalite() {
        return municipalite;
    }
    public void setMunicipalite(String municipalite) {
        this.municipalite = municipalite;
    }
    public String getAdresse() {
        return adresse;
    }
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }


    //public String getUrlimage() { return urlimage; }

    //public void setUrlimage(String urlimage) { this.urlimage = urlimage; }

    @Override
    public String toString() {
        return "User{" +
                "id_user=" + id_user +
                ", name='" + nom + '\'' +
                //", urlimage='" + urlimage + '\'' +
                '}';
    }


}
