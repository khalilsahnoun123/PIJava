package services.service_cov;

import models.models_cov.Covoiturage;
import com.example.demo.interfaces.interfaces_cov.CrudCovoiturage;
import utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCovoiturage implements CrudCovoiturage<Covoiturage> {

    public Connection conx;
    public Statement stm;


    public ServiceCovoiturage() {
        conx = MyDB.getInstance().getConx();
    }

    @Override
    public void ajouter(Covoiturage c) {
        String req =
                "INSERT INTO covoiturage"
                        + "(id_user,point_de_depart,point_de_destination,prix,nbr_place)"
                        + "VALUES(?,?,?,?,?)";
        try {
            PreparedStatement ps = conx.prepareStatement(req);
            ps.setInt(1, c.getId_user());
            ps.setString(2, c.getPoint_de_depart());
            ps.setString(3, c.getPoint_de_destination());
            ps.setFloat(4, c.getPrix());
            ps.setInt(5, c.getNbr_place());
            ps.executeUpdate();
            System.out.println("Covoiturage Ajoutée !!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Covoiturage c) {
        try {
            String req = "UPDATE covoiturage SET id_user=?, point_de_depart=?, point_de_destination=?, prix=?, nbr_place=? WHERE id=?";
            PreparedStatement pst = conx.prepareStatement(req);
            pst.setInt(6, c.getId());
            pst.setInt(1, c.getId_user());
            pst.setString(2, c.getPoint_de_depart());
            pst.setString(3, c.getPoint_de_destination());
            pst.setFloat(4, c.getPrix());
            pst.setInt(5, c.getNbr_place());
            pst.executeUpdate();
            System.out.println("Covoiturage Modifiée !");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM covoiturage WHERE id=?";
        try {
            PreparedStatement pst = conx.prepareStatement(req);
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Covoiturage suprimée !");

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public List<Covoiturage> Show() {
        List<Covoiturage> list = new ArrayList<>();

        try {
            String req = "SELECT * from covoiturage";
            Statement st = conx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                list.add(new Covoiturage(rs.getInt("id"), rs.getInt("id_user"),
                        rs.getString("point_de_depart"), rs.getString("point_de_destination"),
                        rs.getFloat("prix"), rs.getInt("nbr_place")));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return list;
    }

    public Covoiturage getById(int id) throws SQLException {
        Covoiturage cov = null;
        String sql = "SELECT * FROM covoiturage WHERE id = ?";
        try {
            PreparedStatement pst = conx.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                cov=new Covoiturage(rs.getInt("id"), rs.getInt("id_user"),
                        rs.getString("point_de_depart"), rs.getString("point_de_destination"),
                        rs.getFloat("prix"), rs.getInt("nbr_place"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cov;
    }
}
