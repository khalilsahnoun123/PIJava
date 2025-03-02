package com.example.demo.services.services_velo;

import com.example.demo.interfaces.IService;
import com.example.demo.models.models_velo.Velo;
import com.example.demo.models.models_velo.VeloType;
import com.example.demo.models.models_velo.StationVelo;
import com.example.demo.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeloService implements IService<Velo> {

    private Connection cnx;
    private PreparedStatement pst;
    private ResultSet rs;
    private final StationVeloService stationService = new StationVeloService();
    private final VeloTypeService typeService = new VeloTypeService();

    public VeloService() {
        cnx = MyDatabase.getInstance().getConx();
    }

    @Override
    public void insert(Velo velo) {
        String query = "INSERT INTO velo (id_station, id_type, dispo) VALUES (?, ?, ?)";
        try {
            pst = cnx.prepareStatement(query);
            pst.setInt(1, velo.getStation().getIdStation());
            pst.setInt(2, velo.getType().getIdType());
            pst.setBoolean(3, velo.isDispo());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Velo velo) {
        String query = "UPDATE velo SET id_station=?, id_type=?, dispo=? WHERE id_velo=?";
        try {
            pst = cnx.prepareStatement(query);
            pst.setInt(1, velo.getStation().getIdStation());
            pst.setInt(2, velo.getType().getIdType());
            pst.setBoolean(3, velo.isDispo());
            pst.setInt(4, velo.getIdVelo());

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Velo velo) {
        String query = "DELETE FROM velo WHERE id_velo=?";
        try {
            pst = cnx.prepareStatement(query);
            pst.setInt(1, velo.getIdVelo());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Velo> readAll() {
        List<Velo> velos = new ArrayList<>();
        String query = "SELECT v.*, bt.type_name, bt.price, s.name_station FROM velo v "
                + "JOIN bike_type bt ON v.id_type = bt.id_type "
                + "JOIN stationvelo s ON v.id_station = s.id_station";
        try {
            pst = cnx.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                velos.add(mapResultSetToVelo(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return velos;
    }

    @Override
    public Velo readById(int id) {
        String query = "SELECT v.*, bt.type_name, bt.price, s.name_station FROM velo v "
                + "JOIN bike_type bt ON v.id_type = bt.id_type "
                + "JOIN stationvelo s ON v.id_station = s.id_station "
                + "WHERE v.id_velo=?";
        try {
            pst = cnx.prepareStatement(query);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                return mapResultSetToVelo(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Velo> getByStationId(int stationId) {
        List<Velo> velos = new ArrayList<>();
        String query = "SELECT v.*, bt.type_name, bt.price, s.name_station FROM velo v "
                + "JOIN velo_type bt ON v.id_type = bt.id_type "
                + "JOIN stationvelo s ON v.id_station = s.id_station "
                + "WHERE v.id_station=?";
        try {
            pst = cnx.prepareStatement(query);
            pst.setInt(1, stationId);
            rs = pst.executeQuery();
            while (rs.next()) {
                velos.add(mapResultSetToVelo(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return velos;
    }

    private Velo mapResultSetToVelo(ResultSet rs) throws SQLException {
        StationVelo station = stationService.readById(rs.getInt("id_station"));
        VeloType type = typeService.readById(rs.getInt("id_type"));

        return new Velo(
                rs.getInt("id_velo"),
                station,
                type,
                rs.getBoolean("dispo")
        );
    }
    public List<StationVelo> searchStations(String governorat, String municipality, String address) {
        String query = "SELECT * FROM stationvelo WHERE 1=1";
        List<Object> params = new ArrayList<>();

        if (governorat != null && !governorat.isEmpty()) {
            query += " AND gouvernera = ?";
            params.add(governorat);
        }
        if (municipality != null && !municipality.isEmpty()) {
            query += " AND municapilite = ?";
            params.add(municipality);
        }
        if (address != null && !address.isEmpty()) {
            query += " AND adresse LIKE ?";
            params.add("%" + address + "%");
        }

        try {
            pst = cnx.prepareStatement(query);
            for (int i = 0; i < params.size(); i++) {
                pst.setObject(i + 1, params.get(i));
            }
            rs = pst.executeQuery();

            List<StationVelo> results = new ArrayList<>();
            while (rs.next()) {
                // Map result set to StationVelo
            }
            return results;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Velo> getAvailableBikesByStation(int idStation) {
        List<Velo> availableBikes = new ArrayList<>();
        String query = "SELECT v.*, bt.type_name, bt.price, s.name_station FROM velo v "
                + "JOIN velo_type bt ON v.id_type = bt.id_type "
                + "JOIN stationvelo s ON v.id_station = s.id_station "
                + "WHERE v.id_station = ? AND v.dispo = true";

        try {
            pst = cnx.prepareStatement(query);
            pst.setInt(1, idStation);
            rs = pst.executeQuery();

            while (rs.next()) {
                availableBikes.add(mapResultSetToVelo(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching available bikes: " + e.getMessage(), e);
        }
        return availableBikes;
    }
}