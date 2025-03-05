package models.models_taxi;


 // Adaptez ce package en fonction de votre projet

public class Reservation {
    private String taxiId;
    private String userId;

    public Reservation(String taxiId, String userId) {
        this.taxiId = taxiId;
        this.userId = userId;
    }

    // Getters et setters
    public String getTaxiId() {
        return taxiId;
    }

    public void setTaxiId(String taxiId) {
        this.taxiId = taxiId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
