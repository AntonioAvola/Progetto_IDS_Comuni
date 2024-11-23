package com.unicam.DTO.Response;

import java.util.List;

public class ItineraryResponse {

    private String title;
    private String description;
    private List<String> path;

    public ItineraryResponse(String title,
                             String description,
                             List<String> path){
        this.title = title;
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
