package services.service_taxi;

import models.models_taxi.User;
import utils.MyDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {

    private Connection conx;

    public UserService() {
        conx = MyDB.getInstance().getConx();
    }

    // 🔥 Méthode pour récupérer l'utilisateur
    public User getUserById(int id) {
        User user = null;
        String sql = "SELECT id, nom, email FROM user  " +
                "WHERE id = ?";

        try {
            PreparedStatement pst = conx.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                user = new User(rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
}
