package interfaces.interfaces_cov;

import models.models_cov.ReservationCov;

import java.sql.SQLException;
import java.util.List;

public interface CrudReservationCov <ResCov> {
    public void ajouter(ResCov resCov);
    public void modifier(ResCov resCov);
    public void supprimer(int id) throws SQLException;
    public List<ReservationCov> Show();
}
