package interfaces.interfaces_taxi;

import models.models_taxi.Vehicule;

import java.sql.SQLException;
import java.util.List;

public interface CrudVehicule <Vec> {
    public void ajouter(Vec v);
    public void modifier(Vec v);
    public void supprimer(int id) throws SQLException;
    public List<Vehicule> Show();

}
