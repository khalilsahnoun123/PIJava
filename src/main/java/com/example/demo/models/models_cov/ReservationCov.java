package models.models_cov;

public class ReservationCov {
    private int id;
    private int id_user;
    private int id_cov;
    private String status;
    private int nbr_place;

    public ReservationCov(int id, int id_user, int id_cov, String status, int nbr_place) {
        this.id = id;
        this.id_user = id_user;
        this.id_cov = id_cov;
        this.status = status;
        this.nbr_place = nbr_place;
    }

    public ReservationCov(int id_user, int id_cov, String status, int nbr_place) {
        this.id_user = id_user;
        this.id_cov = id_cov;
        this.status = status;
        this.nbr_place = nbr_place;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getId_cov() {
        return id_cov;
    }

    public void setId_cov(int id_cov) {
        this.id_cov = id_cov;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getNbr_place() {
        return nbr_place;
    }

    public void setNbr_place(int nbr_place) {
        this.nbr_place = nbr_place;
    }
}
