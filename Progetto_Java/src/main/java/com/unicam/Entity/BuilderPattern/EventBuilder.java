package com.unicam.Entity.BuilderPattern;

import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Content.Event;
import com.unicam.Entity.Content.GeoPoint;
import com.unicam.Entity.Time;
import com.unicam.Entity.User;


import java.time.LocalDateTime;

public class EventBuilder implements Builder{

    private Event event = new Event();

    @Override
    public void buildTitle(String title) {
        this.event.setTitle(title);
    }

    @Override
    public void buildDescription(String description) {
        this.event.setDescription(description);
    }

    @Override
    public void buildAuthor(User author) {
        this.event.setAuthor(author);
    }

    @Override
    public void buildStatus(ContentStatus status) {
        this.event.setStatus(status);
    }

    @Override
    public void buildMunicipality(String municipality) {
        this.event.setMunicipality(municipality);
    }

    public void buildDuration(LocalDateTime start, LocalDateTime end){
        Time time = new Time(start, end);
        this.event.setDuration(time);
    }

    public void buildReference(GeoPoint reference){
        this.event.setReference(reference);
    }

    public Event result(){
        return this.event;
    }
}
