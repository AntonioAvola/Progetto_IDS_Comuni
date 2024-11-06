package com.unicam.Entity;

import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Content.GeoPoint;

import javax.swing.text.html.MinimalHTMLWriter;

public class Municipality {

    private long id;
    private String name;
    private GeoPoint reference;
    private ContentStatus status;

    public Municipality(){}
    public Municipality(String name,
                        GeoPoint reference){
        this.name = name;
        this.reference = reference;
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

    public GeoPoint getReference() {
        return reference;
    }

    public void setReference(GeoPoint reference) {
        this.reference = reference;
    }

    public ContentStatus getStatus() {
        return status;
    }

    public void setStatus(ContentStatus status) {
        this.status = status;
    }
}
