package com.unicam.DTO.Request;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class ItineraryRequest {

    private String title;
    private String description;
    private List<Long> path = new ArrayList<>();

    public ItineraryRequest(){}
    public ItineraryRequest(String title, String description, List<Long> path) {
        this.title = title.toUpperCase(Locale.ROOT);
        this.description = description;
        HashSet<Long> set = new HashSet<>(path);
        this.path = new ArrayList<>(set);
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }

    public List<Long> getPath() {
        return path;
    }
    public void setPath(List<Long> path){
        HashSet<Long> set = new HashSet<>(path);
        this.path = new ArrayList<>(set);
    }
}
