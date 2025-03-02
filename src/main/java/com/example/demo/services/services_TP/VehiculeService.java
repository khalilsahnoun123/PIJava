package com.example.demo.services.services_TP;




import com.example.demo.enums.enums_TP.TypeVehicule;
import com.example.demo.interfaces.IService_TP;
import com.example.demo.models.models_TP.Vehicule;
import com.example.demo.utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculeService implements IService_TP<Vehicule> {

    private Connection cnx;

    public VehiculeService() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Vehicule vehicule) {
        String query = "INSERT INTO vehicules (type, nbr_max_passagers_vip, nbr_max_passagers_premium, nbr_max_passagers_economy, places_disponibles_vip, places_disponibles_premium, places_disponibles_economy,ligne_id) VALUES (?, ?, ?, ?, ?, ?, ?,?)";
        try (PreparedStatement pstmt = cnx.prepareStatement(query)) {
            pstmt.setString(1, vehicule.getType().name());
            pstmt.setInt(2, vehicule.getNbrMaxPassagersVIP());
            pstmt.setInt(3, vehicule.getNbrMaxPassagersPremium());
            pstmt.setInt(4, vehicule.getNbrMaxPassagersEconomy());
            pstmt.setInt(5, vehicule.getPlacesDisponiblesVIP());
            pstmt.setInt(6, vehicule.getPlacesDisponiblesPremium());
            pstmt.setInt(7, vehicule.getPlacesDisponiblesEconomy());
            pstmt.setInt(8, vehicule.getLigne().getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public Vehicule getVehiculeById(int id) {
        String query = "SELECT * FROM vehicules WHERE id = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToVehicule(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Vehicule> getAll() {
        List<Vehicule> vehicules = new ArrayList<>();
        String query = "SELECT * FROM vehicules";
        try (Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                vehicules.add(mapResultSetToVehicule(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicules;
    }

    @Override
    public void update(Vehicule vehicule) {
        String query = "UPDATE vehicules SET type = ?, nbr_max_passagers_vip = ?, nbr_max_passagers_premium = ?, nbr_max_passagers_economy = ?, places_disponibles_vip = ?, places_disponibles_premium = ?, places_disponibles_economy = ? WHERE id = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(query)) {
            pstmt.setString(1, vehicule.getType().name());
            pstmt.setInt(2, vehicule.getNbrMaxPassagersVIP());
            pstmt.setInt(3, vehicule.getNbrMaxPassagersPremium());
            pstmt.setInt(4, vehicule.getNbrMaxPassagersEconomy());
            pstmt.setInt(5, vehicule.getPlacesDisponiblesVIP());
            pstmt.setInt(6, vehicule.getPlacesDisponiblesPremium());
            pstmt.setInt(7, vehicule.getPlacesDisponiblesEconomy());
            pstmt.setInt(8, vehicule.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM vehicules WHERE id = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Vehicule mapResultSetToVehicule(ResultSet rs) throws SQLException {
        LigneService ligneService = new LigneService();
        Vehicule vehicule = new Vehicule();
        vehicule.setId(rs.getInt("id"));
        vehicule.setType(TypeVehicule.valueOf(rs.getString("type")));
        vehicule.setLigne(ligneService.getLigneById(rs.getInt("ligne_id")));
        vehicule.setNbrMaxPassagersVIP(rs.getInt("nbr_max_passagers_vip"));
        vehicule.setNbrMaxPassagersPremium(rs.getInt("nbr_max_passagers_premium"));
        vehicule.setNbrMaxPassagersEconomy(rs.getInt("nbr_max_passagers_economy"));
        vehicule.setPlacesDisponiblesVIP(rs.getInt("places_disponibles_vip"));
        vehicule.setPlacesDisponiblesPremium(rs.getInt("places_disponibles_premium"));
        vehicule.setPlacesDisponiblesEconomy(rs.getInt("places_disponibles_economy"));
        return vehicule;
    }

    public List<Vehicule> getVehiculesByLineId(int id) {
        List<Vehicule> vehicules = new ArrayList<>();
        String query = "SELECT * FROM vehicules WHERE ligne_id = ?";

        try (PreparedStatement pstmt = cnx.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Vehicule v = mapResultSetToVehicule(rs);
                System.out.println("fetching station from database  : " + v);
                vehicules.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicules;
    }
}

