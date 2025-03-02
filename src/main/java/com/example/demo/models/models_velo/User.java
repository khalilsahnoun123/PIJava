package com.example.demo.models.models_velo;

public class User {
    private  int id_user;
    private String nom;
    //private String urlimage;

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
