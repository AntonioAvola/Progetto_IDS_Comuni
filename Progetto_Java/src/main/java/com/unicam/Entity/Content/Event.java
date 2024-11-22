package com.unicam.Entity.Content;

import com.unicam.Entity.Time;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "event")
public class Event extends Content{

    @Embedded
    private Time duration;
    @ManyToOne
    @JoinColumn(name = "position_Id")
    private GeoPoint reference;
    private ActivityStatus activityStatus;
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

    public ActivityStatus getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(ActivityStatus activityStatus) {
        this.activityStatus = activityStatus;
    }
}
