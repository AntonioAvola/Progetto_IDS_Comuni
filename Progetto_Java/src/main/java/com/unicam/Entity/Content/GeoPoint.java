package com.unicam.Entity.Content;

public class GeoPoint {
    private long id;
    private String name;
    private String municipality;
    private double logitude;
    private double latitude;

    public GeoPoint(String name,
                    String municipality,
                    double logitude,
                    double latitude){
        this.name = name;
        this.municipality = municipality;
        this.logitude = logitude;
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public double getLogitude() {
        return logitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }
}
