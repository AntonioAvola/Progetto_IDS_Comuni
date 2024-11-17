package com.unicam.DTO.Request;

import java.util.Locale;

public class InterestPointRequest {

    private String title;
    private String description;
    private String reference;

    public InterestPointRequest(String title, String description, String reference) {
        this.title = title.toUpperCase(Locale.ROOT);
        this.description = description;
        this.reference = reference.toUpperCase(Locale.ROOT);
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
}
