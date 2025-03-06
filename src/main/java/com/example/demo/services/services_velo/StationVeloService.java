package com.example.demo.services.services_velo;

import com.example.demo.interfaces.IService;
import com.example.demo.models.models_velo.User;
import com.example.demo.models.models_velo.StationVelo;
import com.example.demo.utils.MyDataBase_Velo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StationVeloService implements IService<StationVelo> {

    private Connection cnx;

    private PreparedStatement pst;
    private ResultSet rs;


    public StationVeloService(){
        cnx = MyDataBase_Velo.getInstance().getConx();
    }

    @Override
    public void insert(StationVelo station)  {
        String query = "INSERT INTO stationvelo (name_station, gouvernera, municapilite, adresse, id_admin, station_image) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            pst=cnx.prepareStatement(query);
            pst.setString(1, station.getNameStation());
            pst.setString(2, station.getGouvernera());
            pst.setString(3, station.getMunicapilite());
            pst.setString(4, station.getAdresse());
            pst.setInt(5, station.getAdmin().getId_user());
            pst.setString(6, station.getStationImage());
            pst.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(StationVelo station)  {
        String query = "UPDATE stationvelo SET name_station=?, gouvernera=?, municapilite=?, adresse=?, id_admin=? , station_image=? WHERE id_station=?";
        try {
            pst=cnx.prepareStatement(query);
            pst.setString(1, station.getNameStation());
            pst.setString(2, station.getGouvernera());
            pst.setString(3, station.getMunicapilite());
            pst.setString(4, station.getAdresse());
            pst.setInt(5, station.getAdmin().getId_user());
            pst.setString(6, station.getStationImage());
            pst.setInt(7, station.getIdStation());
            pst.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(StationVelo station)  {
        String query = "DELETE FROM stationvelo WHERE id_station=?";
        try {
            pst=cnx.prepareStatement(query);
            pst.setInt(1, station.getIdStation());
            pst.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<StationVelo> readAll()  {
        List<StationVelo> stations = new ArrayList<>();
        String query = "SELECT * FROM stationvelo";
        try {
            pst = cnx.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                UserService us = new UserService();
                User admin = us.readById(rs.getInt("id_admin"));

                stations.add(new StationVelo(
                        rs.getInt("id_station"),
                        rs.getString("name_station"),
                        rs.getString("gouvernera"),
                        rs.getString("municapilite"),
                        rs.getString("adresse"),
                        admin,
                        rs.getString("station_image")  // Add image path
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading stations: " + e.getMessage(), e);
        }
        return stations;
    }

    @Override
    public StationVelo readById(int id)  {
        String query = "SELECT * FROM stationvelo WHERE id_station=?";
        try {
            pst = cnx.prepareStatement(query);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                UserService us = new UserService();
                User admin = us.readById(rs.getInt("id_admin"));

                return new StationVelo(
                        rs.getInt("id_station"),
                        rs.getString("name_station"),
                        rs.getString("gouvernera"),
                        rs.getString("municapilite"),
                        rs.getString("adresse"),
                        admin,
                        rs.getString("station_image")
                );
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error loading station ID " + id + ": " + e.getMessage(), e);
        }
    }

    public List<String> getAllGovernorats() {
        List<String> governorats = new ArrayList<>();
        String query = "SELECT DISTINCT gouvernera FROM stationvelo ORDER BY gouvernera";

        try (PreparedStatement pst = cnx.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                governorats.add(rs.getString("gouvernera"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading governorats: " + e.getMessage(), e);
        }
        return governorats;
    }

    public List<String> getMunicipalitiesByGovernorat(String governorat) {
        List<String> municipalities = new ArrayList<>();
        String query = "SELECT DISTINCT municapilite FROM stationvelo WHERE gouvernera = ? ORDER BY municapilite";

        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, governorat);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    municipalities.add(rs.getString("municapilite"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading municipalities: " + e.getMessage(), e);
        }
        return municipalities;
    }

    public List<StationVelo> searchStations(String governorat, String municipalite, String adresse) {
        List<StationVelo> stations = new ArrayList<>();
        String baseQuery = "SELECT * FROM stationvelo WHERE 1=1";
        List<Object> params = new ArrayList<>();

        // Construction dynamique de la requête
        if (governorat != null && !governorat.isEmpty()) {
            baseQuery += " AND gouvernera = ?";
            params.add(governorat);
        }

        if (municipalite != null && !municipalite.isEmpty()) {
            baseQuery += " AND municapilite = ?";
            params.add(municipalite);
        }

        if (adresse != null && !adresse.isEmpty()) {
            baseQuery += " AND adresse LIKE ?";
            params.add("%" + adresse + "%");
        }

        try {
            PreparedStatement pst = cnx.prepareStatement(baseQuery);

            // Paramétrage dynamique
            for (int i = 0; i < params.size(); i++) {
                pst.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    StationVelo station = new StationVelo();
                    station.setIdStation(rs.getInt("id_station"));
                    station.setNameStation(rs.getString("name_station"));
                    station.setGouvernera(rs.getString("gouvernera"));
                    station.setMunicapilite(rs.getString("municapilite"));
                    station.setAdresse(rs.getString("adresse"));
                    station.setStationImage(rs.getString("station_image"));

                    // Récupération de l'admin
                    int adminId = rs.getInt("id_admin");
                    User admin = new UserService().readById(adminId);
                    station.setAdmin(admin);

                    stations.add(station);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des stations: " + e.getMessage());
            e.printStackTrace();
        }

        return stations;
    }

    private StationVelo mapResultSetToStation(ResultSet rs) throws SQLException {
        UserService us = new UserService();
        User admin = us.readById(rs.getInt("id_admin"));

        return new StationVelo(
                rs.getInt("id_station"),
                rs.getString("name_station"),
                rs.getString("gouvernera"),
                rs.getString("municapilite"),
                rs.getString("adresse"),
                admin,
                rs.getString("station_image")
        );
    }

    public int getAvailableBikesCount(int idStation) {
        String query = "SELECT COUNT(*) FROM velo WHERE id_station = ? AND dispo = true";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, idStation);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting available bikes count: " + e.getMessage());
        }
        return 0;
    }
}
