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
    private double longitude;
    private double latitude;

    public GeoPoint(String name,
                    String municipality,
                    double longitude,
                    double latitude){
        this.name = name;
        this.municipality = municipality;
        this.longitude = longitude;
        this.latitude = latitude;
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
