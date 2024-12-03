package com.unicam.DTO.Response;

import com.unicam.Entity.Time;

public class EventResponse {

    private long id;
    private String title;
    private String description;
    private Time duration;
    private String reference;

    public EventResponse(long id,
                         String title,
                         String description,
                         Time duration,
                         String reference){
        this.id = id;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.reference = reference;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Time getDuration() {
        return duration;
    }

    public String getReference() {
        return reference;
    }
}
