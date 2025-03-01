package services.service_cov;

import interfaces.interfaces_cov.CrudReservationCov;
import models.models_cov.ReservationCov;
import utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceReservationCov implements CrudReservationCov<ReservationCov> {

    public Connection conx;
    public Statement stm;


    public ServiceReservationCov() {
        conx = MyDB.getInstance().getConx();
    }

    @Override
    public void ajouter(ReservationCov reservationCov) {
        String req =
                "INSERT INTO reservation_cov"
                        + "(id_user,id_cov,status,nbr_place)"
                        + "VALUES(?,?,?,?)";
        try {
            PreparedStatement ps = conx.prepareStatement(req);
            ps.setInt(1, reservationCov.getId_user());
            ps.setInt(2, reservationCov.getId_cov());
            ps.setString(3, reservationCov.getStatus());
            ps.setInt(4, reservationCov.getNbr_place());
            ps.executeUpdate();
            System.out.println("Reservation Cov Ajoutée !!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void modifier(ReservationCov reservationCov) {
        try {
            String req = "UPDATE reservation_cov SET id_user=?, id_cov=?, status=?, nbr_place=? WHERE id=?";
            PreparedStatement pst = conx.prepareStatement(req);
            pst.setInt(5, reservationCov.getId());
            pst.setInt(1, reservationCov.getId_user());
            pst.setInt(2, reservationCov.getId_cov());
            pst.setString(3, reservationCov.getStatus());
            pst.setFloat(4, reservationCov.getNbr_place());
            pst.executeUpdate();
            System.out.println("Reservation Cov Modifiée !");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM reservation_cov WHERE id=?";
        try {
            PreparedStatement pst = conx.prepareStatement(req);
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Reservation Cov suprimée !");

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public List<ReservationCov> Show() {
        List<ReservationCov> list = new ArrayList<>();

        try {
            String req = "SELECT * from reservation_cov";
            Statement st = conx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                list.add(new ReservationCov(rs.getInt("id"), rs.getInt("id_user"),
                        rs.getInt("id_cov"), rs.getString("status"), rs.getInt("nbr_place")));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return list;
    }

    public ReservationCov getById(int id) throws SQLException {
        ReservationCov res_cov = null;
        String sql = "SELECT * FROM reservation_cov WHERE id = ?";
        try {
            PreparedStatement pst = conx.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                res_cov=new ReservationCov(rs.getInt("id"), rs.getInt("id_user"),
                        rs.getInt("id_cov"), rs.getString("status"), rs.getInt("nbr_place"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res_cov;
    }

    public int getNbReservationByIdCov(int id) throws SQLException {
        int NbRes = 0;
        String sql = "SELECT COUNT(*) FROM reservation_cov WHERE id_cov = ?";
        try {
            PreparedStatement pst = conx.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                NbRes=rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return NbRes;
    }

}
