package services.service_taxi;

import interfaces.interfaces_taxi.CrudReservationTaxi;
import models.models_taxi.ReservationTaxi;
import utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceReservationTaxi implements CrudReservationTaxi<ReservationTaxi> {

    public Connection conx;
    public Statement stm;


    public ServiceReservationTaxi() {
        conx = MyDB.getInstance().getConx();
    }

    @Override
    public void ajouter(ReservationTaxi r) {
        String req =
                "INSERT INTO reservation_taxi"
                        + "(id_vehicule,status,id_user)"
                        + "VALUES(?,?,?)";
        try {
            PreparedStatement ps = conx.prepareStatement(req);
            ps.setInt(1, r.getId_vehicule());
            ps.setString(2, r.getStatus());
            ps.setInt(3, r.getId_user());
            ps.executeUpdate();
            System.out.println("Reservation Taxi Ajoutée !!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void modifier(ReservationTaxi r) {
        try {
            String req = "UPDATE reservation_taxi SET id_vehicule=?, status=?, id_user=? WHERE id=?";
            PreparedStatement pst = conx.prepareStatement(req);
            pst.setInt(4,r.getId());
            pst.setInt(1, r.getId_vehicule());
            pst.setString(2, r.getStatus());
            pst.setInt(3, r.getId_user());
            pst.executeUpdate();
            System.out.println("Reservation Taxi Modifiée !");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM reservation_taxi WHERE id=?";
        try {
            PreparedStatement pst = conx.prepareStatement(req);
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Reservation Taxi suprimée !");

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public List<ReservationTaxi> Show() {
        List<ReservationTaxi> list = new ArrayList<>();

        try {
            String req = "SELECT * from reservation_taxi";
            Statement st = conx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                list.add(new ReservationTaxi(rs.getInt("id"), rs.getInt("id_vehicule"),
                        rs.getString("status"), rs.getInt("id_user")));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return list;
    }

    public ReservationTaxi getById(int id) throws SQLException {
        ReservationTaxi res_taxi = null;
        String sql = "SELECT * FROM reservation_taxi WHERE id = ?";
        try {
            PreparedStatement pst = conx.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                res_taxi=new ReservationTaxi(rs.getInt("id"), rs.getInt("id_vehicule"),
                        rs.getString("status"), rs.getInt("id_user"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res_taxi;
    }
}
