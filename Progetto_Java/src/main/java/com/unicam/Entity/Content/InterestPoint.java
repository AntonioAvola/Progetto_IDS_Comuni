package com.unicam.Entity.Content;

import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "interest_point")
public class InterestPoint extends Content{

    @ManyToOne
    @JoinColumn(name = "position_Id", nullable = false)
    private GeoPoint reference;
    private InterestPointType type;
    private LocalTime open;
    private LocalTime close;
    private List<Long> idUserFavorites = new ArrayList<>();
    @ElementCollection
    @Lob
    private List<Media> medias = new ArrayList<>();

    public InterestPoint(){
        super();
    }

    public GeoPoint getReference() {
        return reference;
    }

    public void setReference(GeoPoint reference) {
        this.reference = reference;
    }

    public List<Long> getIdUserFavorites() {
        return idUserFavorites;
    }

    public void setIdUserFavorites(List<Long> idUserFavorites) {
        this.idUserFavorites = idUserFavorites;
    }

    public InterestPointType getType() {
        return type;
    }

    public void setType(InterestPointType type) {
        this.type = type;
    }

    public LocalTime getOpen() {
        return open;
    }

    public void setOpen(LocalTime open) {
        this.open = open;
    }

    public LocalTime getClose() {
        return close;
    }

    public void setClose(LocalTime close) {
        this.close = close;
    }

    public List<Media> getMedias() {
        return medias;
    }

    public void setMedias(Media medias) {
        this.medias.add(medias);
    }
}
