package com.example.demo.services.services_TP;




import com.example.demo.enums.enums_TP.ReservationStatus;
import com.example.demo.enums.enums_TP.TicketCategory;
import com.example.demo.interfaces.IService_TP;
import com.example.demo.models.models_TP.Reservation;
import com.example.demo.utils.MyDatabase_TP;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationService implements IService_TP<Reservation> {

    private Connection cnx;

    public ReservationService() {
        cnx = MyDatabase_TP.getInstance().getCnx();
    }

    @Override
    public void add(Reservation reservation) {
        String query = "INSERT INTO reservations (reservation_date, travel_date, number_of_seats, ticket_category, status, total_price,  depart_station_id, fin_station_id) VALUES (?, ?, ?, ?, ?, ?,  ?, ?)";
        try (PreparedStatement pstmt = cnx.prepareStatement(query)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(reservation.getReservationDate()));
            pstmt.setTimestamp(2, Timestamp.valueOf(reservation.getTravelDate()));
            pstmt.setInt(3, reservation.getNumberOfSeats());
            pstmt.setString(4, reservation.getTicketCategory().toString());
            pstmt.setString(5, reservation.getStatus().toString());
            pstmt.setDouble(6, reservation.getTotalPrice());

            pstmt.setInt(7, reservation.getDepartStation().getId());
            pstmt.setInt(8, reservation.getFinStation().getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public Reservation getReservationById(int id) {
        String query = "SELECT * FROM reservations WHERE reservation_id = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToReservation(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Reservation> getAll() {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservations";
        try (Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {

                reservations.add(mapResultSetToReservation(rs));
            }
            System.out.println(reservations);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    @Override
    public void update(Reservation reservation) {
        String query = "UPDATE reservations SET reservation_date = ?, travel_date = ?, number_of_seats = ?, ticket_category = ?, status = ?, total_price = ?, vehicule_id = ?, depart_station_id = ?, fin_station_id = ? WHERE reservation_id = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(query)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(reservation.getReservationDate()));
            pstmt.setTimestamp(2, Timestamp.valueOf(reservation.getTravelDate()));
            pstmt.setInt(3, reservation.getNumberOfSeats());
            pstmt.setString(4, reservation.getTicketCategory().toString());
            pstmt.setString(5, reservation.getStatus().toString());
            pstmt.setDouble(6, reservation.getTotalPrice());
            // Handle null vehicle
            if (reservation.getVehicule() != null) {
                pstmt.setInt(7, reservation.getVehicule().getId());
            } else {
                pstmt.setNull(7, Types.INTEGER);
            }
            pstmt.setInt(8, reservation.getDepartStation().getId());
            pstmt.setInt(9, reservation.getFinStation().getId());
            pstmt.setInt(10, reservation.getReservationId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM reservations WHERE reservation_id = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
        StationService stationService = new StationService();
        Reservation reservation = new Reservation();
        reservation.setReservationId(rs.getInt("reservation_id"));
        reservation.setReservationDate(rs.getTimestamp("reservation_date").toLocalDateTime());
        reservation.setTravelDate(rs.getTimestamp("travel_date").toLocalDateTime());
        reservation.setNumberOfSeats(rs.getInt("number_of_seats"));
        reservation.setTicketCategory(TicketCategory.valueOf(rs.getString("ticket_category")));
        reservation.setStatus(ReservationStatus.valueOf(rs.getString("status")));
        reservation.setTotalPrice(rs.getDouble("total_price"));

        int vehiculeId = rs.getInt("vehicule_id");
        if (vehiculeId > 0) {
           VehiculeService vehiculeService = new VehiculeService();
          reservation.setVehicule( vehiculeService.getVehiculeById(vehiculeId)  );      }

        int departStationId = rs.getInt("depart_station_id");
        if (departStationId > 0) {
           reservation.setDepartStation(stationService.getStationById(departStationId)    );    }

        int finStationId = rs.getInt("fin_station_id");
        if (finStationId > 0) {
            reservation.setFinStation(stationService.getStationById(finStationId)    );
        }

        return reservation;
    }
}

