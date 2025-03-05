package models.models_taxi;

public class ReservationTaxi {
    private int id;
    private int id_vehicule;
    private String status;
    private int id_user;

    public ReservationTaxi(int id, int id_vehicule, String status, int id_user) {
        this.id = id;
        this.id_vehicule = id_vehicule;
        this.status = status;
        this.id_user = id_user;
    }

    public ReservationTaxi(int id_vehicule, String status, int id_user) {
        this.id_vehicule = id_vehicule;
        this.status = status;
        this.id_user = id_user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_vehicule() {
        return id_vehicule;
    }

    public void setId_vehicule(int id_vehicule) {
        this.id_vehicule = id_vehicule;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }
}
