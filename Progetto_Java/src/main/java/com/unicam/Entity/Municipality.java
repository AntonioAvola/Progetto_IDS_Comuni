package com.unicam.Entity;

import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Content.GeoPoint;
import jakarta.persistence.*;

import javax.swing.text.html.MinimalHTMLWriter;

@Entity
@Table
public class Municipality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @OneToOne
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
