package com.unicam.DTO.Response;

import java.time.LocalTime;

public class InterestPointResponse {

    private long id;
    private String title;
    private String description;
    private String reference;
    private LocalTime open;
    private LocalTime close;

    public InterestPointResponse(long id,
                                 String title,
                                 String description,
                                 String reference, LocalTime open, LocalTime close){
        this.id = id;
        this.title = title;
        this.description = description;
        this.reference = reference;
        this.open = open;
        this.close = close;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getReference() {
        return reference;
    }

    public long getId() {
        return id;
    }

    public LocalTime getOpen() {
        return open;
    }

    public LocalTime getClose() {
        return close;
    }
}
