package com.unicam.Entity.Content;

import com.unicam.Entity.Time;

import java.util.ArrayList;
import java.util.List;

public class Event extends Content{

    private Time duration;
    private GeoPoint reference;
    private List<Long> idUserFavorites = new ArrayList<>();

    public Event(){
        super();
    }

    public Time getDuration() {
        return duration;
    }

    public void setDuration(Time duration) {
        this.duration = duration;
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
}