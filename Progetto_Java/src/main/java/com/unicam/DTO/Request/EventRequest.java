package com.unicam.DTO.Request;



import java.time.LocalDateTime;
import java.util.Locale;

public class EventRequest {

    private String title;
    private String description;
    private long  idReference;
    private LocalDateTime start;
    private LocalDateTime end;

    public EventRequest(){}
    public EventRequest(String title, String description, long idReference, LocalDateTime start, LocalDateTime end) {
        this.title = title.toUpperCase(Locale.ROOT);
        this.description = description;
        this.idReference = idReference;
        this.start = start;
        this.end = end;
    }

    public String getTitle() {
        return title.toUpperCase(Locale.ROOT);
    }

    public String getDescription() {
        return description;
    }

    public long getIdReference() {
        return idReference;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }
}
