package com.example.demo.services.services_velo;

import com.example.demo.interfaces.IService;
import com.example.demo.models.models_velo.User;
import com.example.demo.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService implements IService<User> {

    private Connection cnx;


    public UserService() {
        this.cnx= MyDatabase.getInstance().getConx();
    }

    @Override
    public void insert(User user) {

        Connection cnx = MyDatabase.getInstance().getConx();


        if (cnx == null) {
            System.out.println("La connexion à la base de données a échoué !");
            return;
        }


        String sql = "INSERT INTO user ( nom) " +
                "VALUES (?)";

        try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {

            pstmt.setString(1, user.getNom());



            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Utilisateur inséré avec succès !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion de l'utilisateur : " + e.getMessage());
        }

    }

    @Override
    public void update(User user) {
        // Obtenir la connexion à la base de données
        Connection cnx  = MyDatabase.getInstance().getConx();

        // Vérifier si la connexion est valide
        if (cnx == null) {
            System.out.println("La connexion à la base de données a échoué !");
            return;
        }

        // Requête SQL pour mettre à jour un utilisateur
        String sql = "UPDATE user SET nom = ?" +
                "WHERE id = ?";

        try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {
            // Remplir les paramètres de la requête
            pstmt.setString(1, user.getNom());

            //pstmt.setString(12, user.getUrlimage());
            pstmt.setInt(13, user.getId_user()); // ID de l'utilisateur pour la clause WHERE

            // Exécuter la requête
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Utilisateur mis à jour avec succès !");
            } else {
                System.out.println("Aucun utilisateur trouvé avec l'ID spécifié.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
        }

    }

    @Override
    public void delete(User user) {
        Connection cnx = MyDatabase.getInstance().getConx();

        if (cnx == null) {
            System.out.println("La connexion à la base de données a échoué !");
            return;
        }


        String sql = "DELETE FROM user WHERE id = ?";

        try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {

            pstmt.setInt(1, user.getId_user());
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Utilisateur supprimé avec succès !");
            } else {
                System.out.println("Aucun utilisateur trouvé avec l'ID spécifié.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
        }
    }

    @Override
    public List<User> readAll() {
        Connection cnx = MyDatabase.getInstance().getConx();

        if (cnx == null) {
            System.out.println("La connexion à la base de données a échoué !");
            return null;
        }


        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user";

        try (PreparedStatement pstmt = cnx.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {


            while (rs.next()) {
                User user = new User();
                user.setId_user(rs.getInt("id"));
                user.setNom(rs.getString("nom"));



                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }


        return users;
    }

    @Override
    public User readById(int id) {
        // Obtenir la connexion à la base de données
        Connection cnx = MyDatabase.getInstance().getConx();

        // Vérifier si la connexion est valide
        if (cnx == null) {
            System.out.println("La connexion à la base de données a échoué !");
            return null;
        }

        // Requête SQL pour récupérer un utilisateur par son ID
        String sql = "SELECT * FROM user WHERE id = ?";
        User user = null;

        try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {
            // Définir le paramètre ID
            pstmt.setInt(1, id);

            // Exécuter la requête
            ResultSet rs = pstmt.executeQuery();

            // Vérifier si un résultat est trouvé
            if (rs.next()) {
                // Construire un objet User à partir des colonnes de la table
                user = new User();
                user.setId_user(rs.getInt("id"));
                user.setNom(rs.getString("nom"));

            } else {
                System.out.println("Aucun utilisateur trouvé avec l'ID spécifié.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
        }

        // Retourner l'utilisateur trouvé (ou null si aucun utilisateur trouvé)
        return user;

    }
}
