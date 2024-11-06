package com.unicam.Entity.Content;

public class GeoPoint {
    private long id;
    private String name;
    private double logitude;
    private double latitude;

    public GeoPoint(String name,
                    double logitude,
                    double latitude){
        this.name = name;
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
}
