package com.example.demo.models.models_taxi;

public class User {
    int id_user;
    String nom;
    String email;


    public User(int id_user, String nom, String email) {
        this.id_user = id_user;
        this.nom = nom;
        this.email = email;
    }
    public int getId_user() {
        return id_user;
    }
    public void setId_user(int id_user) {
        this.id_user = id_user;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

}
