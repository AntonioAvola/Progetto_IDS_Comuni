package com.unicam.DTO.Request;

import java.util.List;
import java.util.Locale;

public class ItineraryRequest {

    private String title;
    private String description;
    private List<String> path;


    public ItineraryRequest(String title, String description, List<String> path) {
        this.title = title.toUpperCase(Locale.ROOT);
        this.description = description;
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getPath() {
        return path;
    }
}
