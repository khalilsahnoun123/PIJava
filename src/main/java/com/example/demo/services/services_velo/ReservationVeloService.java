package com.example.demo.services.services_velo;

import com.example.demo.interfaces.IService;
import com.example.demo.models.models_velo.ReservationVelo;
import com.example.demo.models.models_velo.User;
import com.example.demo.models.models_velo.Velo;
import com.example.demo.utils.MyDataBase_Velo;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationVeloService implements IService<ReservationVelo> {

    private Connection cnx;
    private PreparedStatement pst;
    private ResultSet rs;
    private final UserService userService = new UserService();
    private final VeloService veloService = new VeloService();

    public ReservationVeloService() {
        cnx = MyDataBase_Velo.getInstance().getConx();
    }

    @Override
    public void insert(ReservationVelo reservation) {
        String query = "INSERT INTO reservationvelo (id_user, id_velo, date_debut, date_fin, statut, paiement_effectue,price) "
                + "VALUES (?, ?, ?, ?, ?, ?,?)";
        try {
            pst = cnx.prepareStatement(query);
            pst.setInt(1, reservation.getUser().getId_user());
            pst.setInt(2, reservation.getVelo().getIdVelo());
            pst.setTimestamp(3, Timestamp.valueOf(reservation.getDateDebut()));
            pst.setTimestamp(4, Timestamp.valueOf(reservation.getDateFin()));
            pst.setString(5, reservation.getStatut());
            pst.setBoolean(6, reservation.isPaiementEffectue());
            pst.setDouble(7,reservation.getPrice());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(ReservationVelo reservation) {
        String query = "UPDATE reservationvelo SET id_user=?, id_velo=?, date_debut=?, date_fin=?, statut=?, paiement_effectue=? "
                + "WHERE id_reservation_velo=?";
        try {
            pst = cnx.prepareStatement(query);
            pst.setInt(1, reservation.getUser().getId_user());
            pst.setInt(2, reservation.getVelo().getIdVelo());
            pst.setTimestamp(3, Timestamp.valueOf(reservation.getDateDebut()));
            pst.setTimestamp(4, Timestamp.valueOf(reservation.getDateFin()));
            pst.setString(5, reservation.getStatut());
            pst.setBoolean(6, reservation.isPaiementEffectue());
            pst.setInt(7, reservation.getIdReservationVelo());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(ReservationVelo reservation) {
        String query = "DELETE FROM reservationvelo WHERE id_reservation_velo=?";
        try {
            pst = cnx.prepareStatement(query);
            pst.setInt(1, reservation.getIdReservationVelo());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ReservationVelo> readAll() {
        List<ReservationVelo> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservationvelo";
        try {
            pst = cnx.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reservations;
    }

    @Override
    public ReservationVelo readById(int id) {
        String query = "SELECT * FROM reservationvelo WHERE id_reservation_velo=?";
        try {
            pst = cnx.prepareStatement(query);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                return mapResultSetToReservation(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ReservationVelo> getReservationsByUser(User user) {
        List<ReservationVelo> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservationvelo WHERE id_user=?";
        try {
            pst = cnx.prepareStatement(query);
            pst.setInt(1, user.getId_user());
            rs = pst.executeQuery();
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reservations;
    }

    public List<ReservationVelo> getReservationsByVelo(Velo velo) {
        List<ReservationVelo> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservationvelo WHERE id_velo=?";
        try {
            pst = cnx.prepareStatement(query);
            pst.setInt(1, velo.getIdVelo());
            rs = pst.executeQuery();
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reservations;
    }

    private ReservationVelo mapResultSetToReservation(ResultSet rs) throws SQLException {
        User user = userService.readById(rs.getInt("id_user"));
        Velo velo = veloService.readById(rs.getInt("id_velo"));

        return new ReservationVelo(
                rs.getInt("id_reservation_velo"),
                user,
                velo,
                rs.getTimestamp("date_debut").toLocalDateTime(),
                rs.getTimestamp("date_fin").toLocalDateTime(), // Modification ici
                rs.getString("statut"),
                rs.getBoolean("paiement_effectue"),
                rs.getDouble("price") // Ajout du prix
        );
    }
    public boolean isBikeAvailable(Velo velo, LocalDateTime start, LocalDateTime end) {
        String query = "SELECT COUNT(*) FROM reservationvelo WHERE id_velo = ? "
                + "AND NOT (date_fin < ? OR date_debut > ?)";

        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, velo.getIdVelo());
            pst.setTimestamp(2, Timestamp.valueOf(end));
            pst.setTimestamp(3, Timestamp.valueOf(start));

            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() && rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Availability check failed", e);
        }
    }

    // In ReservationVeloService.java
    public boolean createReservation(ReservationVelo reservation) {
        String query = "INSERT INTO reservationvelo (id_user, id_velo, date_debut, date_fin, price) "
                + "VALUES (?, ?, ?, ?, ?)";

        try {
            pst = cnx.prepareStatement(query);

            pst.setInt(1, reservation.getUser().getId_user());
            pst.setInt(2, reservation.getVelo().getIdVelo());
            pst.setTimestamp(3, Timestamp.valueOf(reservation.getDateDebut()));
            pst.setTimestamp(4, Timestamp.valueOf(reservation.getDateFin()));
            pst.setDouble(5, reservation.getPrice());

            pst.executeUpdate();
            updateBikeAvailability(reservation.getVelo().getIdVelo(), false);
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur de création de réservation: " + e.getMessage());
            return false;
        }
    }
    private boolean updateBikeAvailability(int idVelo, boolean available) {
        String query = "UPDATE velo SET dispo = ? WHERE id_velo = ?";

        try {
            pst = cnx.prepareStatement(query);

            pst.setBoolean(1, available);
            pst.setInt(2, idVelo);

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur de mise à jour de disponibilité", e);
        }
    }
}