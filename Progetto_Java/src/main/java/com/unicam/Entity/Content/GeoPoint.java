package com.unicam.Entity.Content;

import jakarta.persistence.*;

@Entity
@Table(name = "geo_point")
public class GeoPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String municipality;
    private double latitude;
    private double longitude;

    public GeoPoint(){}
    public GeoPoint(String name,
                    String municipality,
                    double latitude,
                    double longitude){
        this.name = name;
        this.municipality = municipality;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public double getLongitude() {
        return longitude;
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
