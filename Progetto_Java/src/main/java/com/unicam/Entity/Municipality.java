package com.unicam.Entity;

import com.unicam.DTO.MunicipalityDetails;
import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Content.GeoPoint;
import jakarta.persistence.*;

import java.util.List;
import java.util.Map;

@Entity
@Table
public class Municipality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    /*@OneToOne
    private GeoPoint referenceMax;
    @OneToOne
    private GeoPoint referenceMin;*/

    private double latitudeMin;
    private double latitudeMax;
    private double longitudeMin;
    private double longitudeMax;

    private ContentStatus status;
    private Long population;
    private String description;
    private String province;
    private String region;
    private String country;
    private Long postCode;

    //TODO aggiungere file multimediali

    public Municipality(){}

    public Municipality(String name, String description,
                        MunicipalityDetails details){
        this.name = name;
        this.description = description;
        this.population = details.getPopulation();
        this.province = details.getProvince();
        this.region = details.getRegion();
        this.country = details.getCountry();
        this.postCode = details.getPostCode();
        this.latitudeMin = details.getSurface().get("coordinate minime").get(0);
        this.latitudeMax = details.getSurface().get("coordinate massime").get(0);
        this.longitudeMin = details.getSurface().get("coordinate minime").get(1);
        this.longitudeMax = details.getSurface().get("coordinate massime").get(1);
        this.status = ContentStatus.PENDING;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ContentStatus getStatus() {
        return status;
    }

    public void setStatus(ContentStatus status) {
        this.status = status;
    }

    public Long getPopulation() {
        return population;
    }

    public String getDescription() {
        return description;
    }

    public String getProvince() {
        return province;
    }

    public String getRegion() {
        return region;
    }

    public String getCountry() {
        return country;
    }

    public Long getPostCode() {
        return postCode;
    }

    public double getLatitudeMin() {
        return latitudeMin;
    }

    public double getLatitudeMax() {
        return latitudeMax;
    }

    public double getLongitudeMin() {
        return longitudeMin;
    }

    public double getLongitudeMax() {
        return longitudeMax;
    }
}
