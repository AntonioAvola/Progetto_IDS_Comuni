package com.unicam.DTO.Request;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class ItineraryRequest {

    private String title;
    private String description;
    private List<Long> path;


    public ItineraryRequest(String title, String description, List<Long> path) {
        this.title = title.toUpperCase(Locale.ROOT);
        this.description = description;
        HashSet<Long> set = new HashSet<>(path);
        this.path = new ArrayList<>(set);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Long> getPath() {
        return path;
    }
}
