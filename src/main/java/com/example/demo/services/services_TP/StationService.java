package com.example.demo.services.services_TP;



import com.example.demo.interfaces.IService;
import com.example.demo.models.models_TP.Station;
import com.example.demo.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StationService implements IService<Station> {
    private Connection cnx;

    public StationService() {
        cnx = MyDatabase.getInstance().getCnx();
    }


    @Override
    public void add(Station station) {
        String query = "INSERT INTO stations (nom, adresse,ligne_id) VALUES (?, ?,?)";
        try (PreparedStatement pstmt = cnx.prepareStatement(query)) {
            pstmt.setString(1, station.getNom());
            pstmt.setString(2, station.getAdresse());
            pstmt.setInt(3, station.getLigne().getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Station getStationById(int id) {
        String query = "SELECT * FROM stations WHERE id = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToStation(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<Station> getStationByIdLigne(int id) {
        List<Station> stations = new ArrayList<>();
        String query = "SELECT * FROM stations WHERE ligne_id = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Station station = mapResultSetToStation(rs);
                System.out.println("fetching station from database  : " + station);
                stations.add(station);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stations;
    }


    @Override
    public List<Station> getAll() {
        List<Station> stations = new ArrayList<>();
        String query = "SELECT * FROM stations";
        try (Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                stations.add(mapResultSetToStation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stations;
    }

    @Override
    public void update(Station station) {
        String query = "UPDATE stations SET nom = ?, adresse = ?,ligne_id= ? WHERE id = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(query)) {
            pstmt.setString(1, station.getNom());
            pstmt.setString(2, station.getAdresse());
            pstmt.setInt(3, station.getLigne().getId());
            pstmt.setInt(4, station.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM stations WHERE id = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Station mapResultSetToStation(ResultSet rs) throws SQLException {
        LigneService ligneService = new LigneService();
        Station station = new Station();
        station.setId(rs.getInt("id"));
        station.setNom(rs.getString("nom"));
        station.setAdresse(rs.getString("adresse"));
        station.setLigne(ligneService.getLigneById(rs.getInt("ligne_id")));
        return station;
    }
}
