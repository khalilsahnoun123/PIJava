package com.example.demo.services.services_TP;

// Adjust the package if needed


import com.example.demo.enums.enums_TP.Region;
import com.example.demo.interfaces.IService_TP;
import com.example.demo.models.models_TP.Ligne;
import com.example.demo.utils.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LigneService implements IService_TP<Ligne> {
    private Connection cnx;

    public LigneService() {
        cnx = MyDatabase_TP.getInstance().getCnx();
    }

    @Override

    public void add(Ligne ligne) {
        String query =  "INSERT INTO ligne (name, prix_vip, prix_premium, prix_economique, region) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = cnx.prepareStatement(query)) {
            pstmt.setString(1, ligne.getName());
            pstmt.setDouble(2, ligne.getPrixVIP());
            pstmt.setDouble(3, ligne.getPrixPREMIUM());
            pstmt.setDouble(4, ligne.getPrixECONIMIC());
            pstmt.setString(5, ligne.getRegion().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Ligne getLigneById(int id) {
        String query = "SELECT * FROM ligne WHERE id = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToLigne(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Ligne> getAll() {
        List<Ligne> lignes = new ArrayList<>();
        String query = "SELECT * FROM ligne";
        try (Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                lignes.add(mapResultSetToLigne(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lignes;
    }

    @Override
    public void update(Ligne ligne) {
        String query = "UPDATE ligne SET name = ?, prix_vip = ?, prix_premium = ?, prix_economique = ?, region = ? WHERE id = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(query)) {
            pstmt.setString(1, ligne.getName());
            pstmt.setDouble(2, ligne.getPrixVIP());
            pstmt.setDouble(3, ligne.getPrixPREMIUM());
            pstmt.setDouble(4, ligne.getPrixECONIMIC());
            pstmt.setString(5, ligne.getRegion().toString());
            pstmt.setInt(6, ligne.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM ligne WHERE id = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Ligne mapResultSetToLigne(ResultSet rs) throws SQLException {
        Ligne ligne = new Ligne();
        ligne.setId(rs.getInt("id"));
        ligne.setName(rs.getString("name"));
        ligne.setPrixVIP(rs.getDouble("prix_vip"));
        ligne.setPrixPREMIUM(rs.getDouble("prix_premium"));
        ligne.setPrixECONIMIC(rs.getDouble("prix_economique"));
        ligne.setRegion(Region.valueOf(rs.getString("region")));
        // Remarque : Les listes de stations et de véhicules ne sont pas chargées ici.
        return ligne;
    }

}
