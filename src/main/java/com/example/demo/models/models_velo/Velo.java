package com.example.demo.models.models_velo;

public class Velo {
    private int idVelo;
    private StationVelo station;
    private VeloType type;
    private boolean dispo;

    public Velo() {}

    public Velo(int idVelo, StationVelo station, VeloType type,
                boolean dispo) {
        this.idVelo = idVelo;
        this.station = station;
        this.type = type;
        this.dispo = dispo;
    }

    // Getters and setters
    public int getIdVelo() { return idVelo; }
    public void setIdVelo(int idVelo) { this.idVelo = idVelo; }

    public StationVelo getStation() { return station; }
    public void setStation(StationVelo station) { this.station = station; }

    public VeloType getType() { return type; }
    public void setType(VeloType type) { this.type = type; }

    public boolean isDispo() { return dispo; }
    public void setDispo(boolean dispo) { this.dispo = dispo; }



    @Override
    public String toString() {
        return "Velo " + idVelo + " - " + type.getTypeName() +
                " | Station: " + station.getNameStation() +
                " | Prix: " + type.getPrice() + " DT/h";
    }
}