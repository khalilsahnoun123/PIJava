package com.example.demo.services.services_velo;

import com.example.demo.interfaces.IService;
import com.example.demo.models.models_velo.VeloType;
import com.example.demo.utils.MyDataBase_Velo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeloTypeService implements IService<VeloType> {

    private Connection cnx;
    private PreparedStatement pst;
    private ResultSet rs;

    public VeloTypeService() {
        cnx = MyDataBase_Velo.getInstance().getConx();
    }

    public VeloType readById(int id) {
        String query = "SELECT * FROM velo_type WHERE id_type=?";
        try {
            pst = cnx.prepareStatement(query);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                return new VeloType(
                        rs.getInt("id_type"),
                        rs.getString("type_name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getString("velo_image")
                );
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insert(VeloType veloType) {
        String query = "INSERT INTO velo_type (type_name, description, price,velo_image) VALUES (?, ?, ?,?)";
        try {
            pst = cnx.prepareStatement(query);
            pst.setString(1, veloType.getTypeName());
            pst.setString(2, veloType.getDescription());
            pst.setDouble(3, veloType.getPrice());
            pst.setString(4, veloType.getVeloImage());

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(VeloType veloType) {
        String query = "UPDATE velo_type SET type_name=?, description=?, price=?,velo_image=? WHERE id_type=?";
        try {
            pst = cnx.prepareStatement(query);
            pst.setString(1, veloType.getTypeName());
            pst.setString(2, veloType.getDescription());
            pst.setDouble(3, veloType.getPrice());
            pst.setString(4, veloType.getVeloImage());
            pst.setInt(5, veloType.getIdType()); // Paramètre manquant

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(VeloType type) {
        // Vérifier d'abord les dépendances
        if (isTypeUsedInVelos(type.getIdType())) {
            throw new RuntimeException("Ce type est utilisé par des vélos existants !");
        }

        String query = "DELETE FROM velo_type WHERE id_type=?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, type.getIdType());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur de suppression : " + e.getMessage());
        }
    }

    private boolean isTypeUsedInVelos(int typeId) {
        String query = "SELECT COUNT(*) FROM velo WHERE id_type=?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, typeId);
            ResultSet rs = pst.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur de vérification : " + e.getMessage());
        }
    }

    @Override
    public List<VeloType> readAll() {
        List<VeloType> types = new ArrayList<>();
        String query = "SELECT * FROM velo_type";
        try {
            pst = cnx.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                types.add(new VeloType(
                        rs.getInt("id_type"),
                        rs.getString("type_name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getString("velo_image")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading bike types: " + e.getMessage(), e);
        }
        return types;
    }


}