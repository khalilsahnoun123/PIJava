package com.example.demo.models.models_velo;

public class VeloType {
    private int idType;
    private String typeName;
    private String description;
    private double price;  // New price field
    private String veloImage;

    public VeloType() {}

    public VeloType(int idType, String typeName, String description, double price,String veloImage) {
        this.idType = idType;
        this.typeName = typeName;
        this.description = description;
        this.price = price;
        this.veloImage = veloImage;

    }

    // Getters and setters
    public int getIdType() { return idType; }
    public void setIdType(int idType) { this.idType = idType; }

    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getVeloImage() { return veloImage; }
    public void setVeloImage(String veloImage) { this.veloImage = veloImage; }

    @Override
    public String toString() {
        return typeName + " (" + price + " DT/heure)";
    }
}