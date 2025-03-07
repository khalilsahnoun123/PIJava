package com.example.demo.interfaces.interfaces_taxi;


import com.example.demo.models.models_taxi.ReservationTaxi;

import java.sql.SQLException;
import java.util.List;

public interface CrudReservationTaxi <Res> {
    public void ajouter(Res r);
    public void modifier(Res r);
    public void supprimer(int id) throws SQLException;
    public List<ReservationTaxi> Show();


}
