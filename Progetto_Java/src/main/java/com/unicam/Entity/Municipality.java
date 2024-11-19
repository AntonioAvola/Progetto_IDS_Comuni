package com.unicam.Entity;

import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Content.GeoPoint;
import jakarta.persistence.*;

@Entity
@Table
public class Municipality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    //TODO provare a cambiare da tue attributi distinti a una Map<String, Double>
    @OneToOne
    private GeoPoint referenceMax;
    @OneToOne
    private GeoPoint referenceMin;
    private ContentStatus status;
    private long population;
    private String description;
    private String province;
    private String region;
    private String postCode;

    //TODO aggiungere file multimediali

    public Municipality(){}
    public Municipality(String name,
                        GeoPoint referenceMax,
                        GeoPoint referenceMin){
        this.name = name;
        this.referenceMax = referenceMax;
        this.referenceMin = referenceMin;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GeoPoint getReferenceMax() {
        return referenceMax;
    }

    public void setReferenceMax(GeoPoint referenceMax) {
        this.referenceMax = referenceMax;
    }

    public ContentStatus getStatus() {
        return status;
    }

    public void setStatus(ContentStatus status) {
        this.status = status;
    }

    public GeoPoint getReferenceMin() {
        return referenceMin;
    }
}
