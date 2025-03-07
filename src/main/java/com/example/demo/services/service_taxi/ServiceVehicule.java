package com.example.demo.services.service_taxi;


import com.example.demo.interfaces.interfaces_taxi.CrudVehicule;
import com.example.demo.models.models_taxi.Vehicule;
import utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceVehicule implements CrudVehicule<Vehicule> {

    public Connection conx;
    public Statement stm;


    public ServiceVehicule() {
        conx = MyDB.getInstance().getConx();
    }
    @Override
    public void ajouter(Vehicule v) {
        String req =
                "INSERT INTO vehicule"
                        + "(id_chauffeur,marque,modele,immatriculation,numero_de_serie)"
                        + "VALUES(?,?,?,?,?)";
        try {
            PreparedStatement ps = conx.prepareStatement(req);
            ps.setInt(1, v.getId_chauffeur());
            ps.setString(2, v.getMarque());
            ps.setString(3, v.getModele());
            ps.setString(4, v.getImmatriculation());
            ps.setString(5, v.getNumero_de_serie());
            ps.executeUpdate();
            System.out.println("Vehicule Ajoutée !!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Vehicule v) {
        try {
            String req = "UPDATE vehicule SET id_chauffeur=?, marque=?, modele=?, immatriculation=?, numero_de_serie=? WHERE id=?";
            PreparedStatement pst = conx.prepareStatement(req);
            pst.setInt(6, v.getId());
            pst.setInt(1, v.getId_chauffeur());
            pst.setString(2, v.getMarque());
            pst.setString(3, v.getModele());
            pst.setString(4, v.getImmatriculation());
            pst.setString(5, v.getNumero_de_serie());
            pst.executeUpdate();
            System.out.println("Vehicule Modifiée !");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM vehicule WHERE id=?";
        try {
            PreparedStatement pst = conx.prepareStatement(req);
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Vehicule suprimée !");

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public List<Vehicule> Show() {
        List<Vehicule> list = new ArrayList<>();

        try {
            String req = "SELECT * from vehicule";
            Statement st = conx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                list.add(new Vehicule(rs.getInt("id"), rs.getInt("id_chauffeur"),
                        rs.getString("marque"), rs.getString("modele"),
                        rs.getString("immatriculation"), rs.getString("numero_de_serie")));
            }
            System.out.println("Liste des vehicules !" + list);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return list;
    }

    public Vehicule getById(int id) throws SQLException {
        Vehicule vec = null;
        String sql = "SELECT * FROM vehicule WHERE id = ?";
        try {
            PreparedStatement pst = conx.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                vec=new Vehicule(rs.getInt("id"), rs.getInt("id_chauffeur"),
                        rs.getString("marque"), rs.getString("modele"),
                        rs.getString("immatriculation"), rs.getString("numero_de_serie"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vec;
    }
}
