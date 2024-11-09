package com.unicam.DTO;



import java.time.LocalDateTime;

public class EventRequest {

    private String title;
    private String description;
    private String reference;
    private LocalDateTime start;
    private LocalDateTime end;

    public EventRequest(String title, String description, String reference, LocalDateTime start, LocalDateTime end) {
        this.title = title;
        this.description = description;
        this.reference = reference;
        this.start = start;
        this.end = end;
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

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }
}
